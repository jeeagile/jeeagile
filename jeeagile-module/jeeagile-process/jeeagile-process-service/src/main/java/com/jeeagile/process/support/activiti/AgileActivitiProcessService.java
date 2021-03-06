package com.jeeagile.process.support.activiti;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.util.AgileDateUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.AgileSysUserPost;
import com.jeeagile.frame.entity.system.AgileSysUserRole;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysUserPostService;
import com.jeeagile.frame.service.system.IAgileSysUserRoleService;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.service.IAgileProcessInstanceService;
import com.jeeagile.process.support.IAgileProcessService;
import com.jeeagile.process.vo.AgileProcessHistory;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AgileActivitiProcessService implements IAgileProcessService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;

    @Lazy
    @Autowired
    private IAgileProcessInstanceService agileProcessInstanceService;

    @Override
    public String processDeployment(AgileProcessModel agileProcessModel) {

        Deployment deployment = repositoryService.createDeployment()
                .addString(agileProcessModel.getId() + ".bpmn", agileProcessModel.getModelXml())
                .name(agileProcessModel.getModelName())
                .key(agileProcessModel.getModelCode())
                .deploy();
        return deployment.getId();
    }

    @Override
    public String getProcessDefinitionId(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        if (processDefinition == null || AgileStringUtil.isEmpty(processDefinition.getId())) {
            throw new AgileFrameException("?????????????????????");
        }
        return processDefinition.getId();
    }

    @Override
    public boolean checkProcessDefinition(String definitionId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(definitionId);
        if (processDefinition == null) {
            throw new AgileValidateException("???????????????????????????");
        }
        if (processDefinition.isSuspended()) {
            throw new AgileValidateException("?????????????????????????????????");
        }
        return true;
    }

    @Override
    public boolean processDefinitionActive(String definitionId) {
        return updateSuspensionState(definitionId, SuspensionState.ACTIVE);
    }

    @Override
    public boolean processDefinitionSuspend(String definitionId) {
        return updateSuspensionState(definitionId, SuspensionState.SUSPENDED);
    }

    @Override
    public boolean startProcessInstance(String definitionId, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definitionId, variables);
        if (processInstance == null || AgileStringUtil.isEmpty(processInstance.getId())) {
            throw new AgileFrameException("???????????????????????????");
        }
        return true;
    }

    @Override
    public Map<String, Object> getProcessInstanceHighLineData(String definitionId, String instanceId) {
        //??????bpmnModel??????
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        //????????????????????????????????????Process ???????????????????????????????????????
        Process process = bpmnModel.getProcesses().get(0);
        //???????????????FlowElement??????
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //?????????????????????
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //?????????????????? ????????????(??????)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).list();
        //??????????????????   ???????????? key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //????????????ID
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> {
            if (AgileStringUtil.isNotEmpty(map.get(s))) {
                highLine.add(map.get(s));
            }
        });


        //?????????????????? ???????????????????????????
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).finished().list();
        //????????????ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //?????????????????? ??????????????????????????????
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).unfinished().list();
        //???????????????????????????
        Set<String> set = new HashSet<>();
        //??????????????????
        Set<String> waitingPoint = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingPoint.add(s.getActivityId());
            for (FlowElement flowElement : flowElements) {
                //??????????????? ????????????
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;
                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //?????? ????????????????????????????????????  ???????????? ????????? ??????  ????????????????????? ??????????????????  ?????????????????????????????? ?????? ?????????????????????
                        if (outgoingFlows != null && outgoingFlows.size() > 0) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(s.getActivityId())) {
                                    set.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });
        highLine.removeAll(set);


        //??????????????????
        Set<String> handlePoint = new HashSet<>(); //?????? ?????? ??????????????????
        //??????????????????????????????
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(AgileSecurityContext.getUserId())
                .finished()
                .processInstanceId(instanceId).list();

        taskInstanceList.forEach(a -> handlePoint.add(a.getTaskDefinitionKey()));

        Map<String, Object> highLineData = new HashMap();
        highLineData.put("highPoint", highPoint);
        highLineData.put("highLine", highLine);
        highLineData.put("waitingPoint", waitingPoint);
        highLineData.put("handlePoint", handlePoint);
        return highLineData;
    }

    @Override
    public List<AgileProcessHistory> getProcessInstanceHistoric(String instanceId) {
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        List<AgileProcessHistory> agileProcessHistoryList = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            AgileProcessHistory agileProcessHistory = new AgileProcessHistory();
            AgileBeanUtils.copyProperties(historicActivityInstance, agileProcessHistory);
            agileProcessHistory.setDefinitionId(historicActivityInstance.getProcessDefinitionId());
            agileProcessHistory.setInstanceId(historicActivityInstance.getProcessInstanceId());
            if (historicActivityInstance.getActivityType().equals("startEvent")) {
                agileProcessHistory.setDurationTime("0???");
                agileProcessHistory.setStatus("?????????");
                agileProcessHistory.setMessage("????????????");
            } else if (historicActivityInstance.getActivityType().equals("userTask")) {
                if (AgileStringUtil.isNotEmpty(historicActivityInstance.getEndTime())) {
                    agileProcessHistory.setStatus("?????????");
                    if (AgileStringUtil.isNotEmpty(historicActivityInstance.getDeleteReason())) {
                        agileProcessHistory.setMessage(historicActivityInstance.getDeleteReason());
                    } else {
                        List<Comment> commentList = taskService.getTaskComments(historicActivityInstance.getTaskId());
                        StringBuffer message = new StringBuffer("");
                        for (Comment comment : commentList) {
                            message.append(comment.getFullMessage()).append(";");
                        }
                        agileProcessHistory.setMessage(message.toString());
                    }
                    String durationTime = AgileDateUtil.formatDateAgo(historicActivityInstance.getDurationInMillis());
                    agileProcessHistory.setDurationTime(durationTime);
                } else {
                    agileProcessHistory.setStatus("?????????");
                    agileProcessHistory.setDurationTime("--???");
                }
            } else if (historicActivityInstance.getActivityType().equals("endEvent")) {
                agileProcessHistory.setAssigneeName("????????????");
                agileProcessHistory.setStatus("??????");
                agileProcessHistory.setMessage("????????????");
            } else {
                continue;
            }
            agileProcessHistoryList.add(agileProcessHistory);
        }
        return agileProcessHistoryList;
    }

    @Override
    public AgilePage<AgileProcessTask> getUserTodoTask(AgilePageable<AgileProcessTask> agilePageable) {
        List<String> userCandidateGroupList = getUserCandidateGroupList();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(AgileSecurityContext.getUserId(), userCandidateGroupList);
        AgileProcessTask agileProcessTaskCond = agilePageable.getQueryCond();
        if (agileProcessTaskCond != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessTaskCond.getModelName())) {
                taskQuery.processDefinitionNameLike("%" + agileProcessTaskCond.getModelName() + "%");
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTaskCond.getModelCode())) {
                taskQuery.processDefinitionKeyLike("%" + agileProcessTaskCond.getModelCode() + "%");
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTaskCond.getStartUser())) {
                taskQuery.taskOwner(agileProcessTaskCond.getStartUser());
            }
        }
        long totalCount = taskQuery.count();
        AgilePage<AgileProcessTask> agileProcessTaskPage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        agileProcessTaskPage.setTotal(totalCount);
        if (totalCount > 0) {
            taskQuery.orderByTaskCreateTime().desc();
            if (agilePageable.getCurrentPage() < 1) {
                agilePageable.setCurrentPage(1);
            }
            int firstResult = (agilePageable.getCurrentPage() - 1) * agilePageable.getPageSize();
            int maxResults = (agilePageable.getCurrentPage()) * agilePageable.getPageSize();
            List<Task> taskList = taskQuery.listPage(firstResult, maxResults);
            List<AgileProcessTask> agileProcessTaskList = new ArrayList<>();
            taskList.forEach(task -> {
                AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(task.getProcessInstanceId());
                if (agileProcessInstance != null && agileProcessInstance.isNotEmptyPk()) {
                    AgileProcessTask agileProcessTask = new AgileProcessTask();
                    agileProcessTask.setId(task.getId());
                    agileProcessTask.setInstanceId(agileProcessInstance.getId());
                    agileProcessTask.setModelCode(agileProcessInstance.getModelCode());
                    agileProcessTask.setModelName(agileProcessInstance.getModelName());
                    agileProcessTask.setFormName(agileProcessInstance.getFormName());
                    agileProcessTask.setTaskName(task.getName());
                    agileProcessTask.setTaskStatus("1");
                    agileProcessTask.setStartUser(agileProcessInstance.getStartUser());
                    agileProcessTask.setStartUserName(agileProcessInstance.getStartUserName());
                    agileProcessTask.setStartTime(task.getCreateTime());
                    agileProcessTaskList.add(agileProcessTask);
                }
            });
            agileProcessTaskPage.setRecords(agileProcessTaskList);
        }

        return agileProcessTaskPage;
    }

    private List<String> getUserCandidateGroupList() {
        List<String> userCandidateGroupList = new ArrayList<>();
        AgileBaseUser agileUserData = AgileSecurityContext.getUserData();
        userCandidateGroupList.add("dept:" + agileUserData.getDeptId());
        userCandidateGroupList.addAll(this.getUserRoleList(agileUserData.getUserId()));
        userCandidateGroupList.addAll(this.getUserPostList(agileUserData.getUserId()));
        return userCandidateGroupList;
    }

    private List<String> getUserRoleList(String userId) {
        List<String> userRoleList = new ArrayList<>();
        LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
        agileSysUserRoleService.list(lambdaQueryWrapper).forEach(agileSysUserRole ->
                userRoleList.add("role:" + agileSysUserRole.getRoleId())
        );
        return userRoleList;
    }

    private List<String> getUserPostList(String userId) {
        List<String> userPostList = new ArrayList<>();
        LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
        agileSysUserPostService.list(lambdaQueryWrapper).forEach(agileSysUserPost ->
                userPostList.add("post:" + agileSysUserPost.getPostId())
        );
        return userPostList;
    }

    @Override
    public boolean cancelProcessInstance(String instanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(instanceId, deleteReason);
        return true;
    }

    @Override
    public boolean approveProcessTask(String instanceId, String taskId, String approveMessage) {
        return handlerProcessTask(instanceId, taskId, approveMessage, true);
    }

    @Override
    public boolean refuseProcessTask(String instanceId, String taskId, String approveMessage) {
        return handlerProcessTask(instanceId, taskId, approveMessage, false);
    }

    private boolean handlerProcessTask(String instanceId, String taskId, String approveMessage, boolean flag) {
        // ????????????????????????
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if (processInstance == null) {
            throw new AgileFrameException("???????????????????????????");
        }
        // ??????????????????
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new AgileFrameException("???????????????????????????");
        }
        if (AgileStringUtil.isEmpty(task.getAssignee())) {
            taskService.claim(task.getId(), AgileSecurityContext.getUserId());
        }
        taskService.addComment(task.getId(), processInstance.getId(), approveMessage);
        if (flag) {
            taskService.complete(task.getId(), processInstance.getProcessVariables());
        } else {
            runtimeService.deleteProcessInstance(processInstance.getId(), approveMessage);
        }
        return true;
    }

    /**
     * ????????????????????????????????????
     */
    private boolean updateSuspensionState(String definitionId, SuspensionState suspensionState) {
        if (AgileStringUtil.isNotEmpty(definitionId)) {
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(definitionId);
            if (processDefinition != null) {
                if (SuspensionState.ACTIVE.equals(suspensionState)) {
                    repositoryService.activateProcessDefinitionById(processDefinition.getId(), false, new Date());
                }
                if (SuspensionState.SUSPENDED.equals(suspensionState)) {
                    repositoryService.suspendProcessDefinitionById(processDefinition.getId(), false, new Date());
                }
                return true;
            } else {
                throw new AgileValidateException("????????????????????????");
            }
        } else {
            throw new AgileValidateException("????????????ID???????????????");
        }
    }
}
