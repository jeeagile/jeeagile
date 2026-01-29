package com.jeeagile.process.support.activiti;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
            throw new AgileFrameException("流程定义不存在");
        }
        return processDefinition.getId();
    }

    @Override
    public boolean checkProcessDefinition(String definitionId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(definitionId);
        if (processDefinition == null) {
            throw new AgileValidateException("流程定义已不存在！");
        }
        if (processDefinition.isSuspended()) {
            throw new AgileValidateException("流程定义处于挂起状态！");
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
    public String startProcessInstance(String definitionId, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definitionId, variables);
        if (processInstance == null || AgileStringUtil.isEmpty(processInstance.getId())) {
            throw new AgileFrameException("流程定义启动失败！");
        }
        return processInstance.getId();
    }

    @Override
    public Map<String, Object> getProcessInstanceHighLineData(String definitionId, String instanceId) {
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //判断是否是连线
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //获取流程实例 历史节点(全部)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).list();
        //各个历史节点   两两组合 key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //高亮连线ID
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> {
            if (AgileStringUtil.isNotEmpty(map.get(s))) {
                highLine.add(map.get(s));
            }
        });


        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).finished().list();
        //高亮节点ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).unfinished().list();
        //需要移除的高亮连线
        Set<String> set = new HashSet<>();
        //待办高亮节点
        Set<String> waitingPoint = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingPoint.add(s.getActivityId());
            for (FlowElement flowElement : flowElements) {
                //判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;
                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
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


        //获取当前用户
        Set<String> handlePoint = new HashSet<>(); //存放 高亮 我的办理节点
        //当前用户已完成的任务
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
        AgileProcessHistory startAgileProcessHistory = new AgileProcessHistory();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            AgileProcessHistory agileProcessHistory = new AgileProcessHistory();
            AgileBeanUtils.copyProperties(historicActivityInstance, agileProcessHistory);
            agileProcessHistory.setDefinitionId(historicActivityInstance.getProcessDefinitionId());
            agileProcessHistory.setInstanceId(historicActivityInstance.getProcessInstanceId());
            if (historicActivityInstance.getActivityType().equals("startEvent")) {
                agileProcessHistory.setDurationTime("0秒");
                agileProcessHistory.setStatus("已完成");
                agileProcessHistory.setMessage("发起流程");
                startAgileProcessHistory = agileProcessHistory;
                continue;
            } else if (historicActivityInstance.getActivityType().equals("userTask")) {
                if (AgileStringUtil.isNotEmpty(historicActivityInstance.getEndTime())) {
                    agileProcessHistory.setStatus("已完成");
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
                    agileProcessHistory.setStatus("办理中");
                    agileProcessHistory.setDurationTime("--秒");
                }
            } else if (historicActivityInstance.getActivityType().equals("endEvent")) {
                agileProcessHistory.setAssigneeName("系统执行");
                agileProcessHistory.setStatus("结束");
                agileProcessHistory.setMessage("结束流程");
            } else {
                continue;
            }
            agileProcessHistoryList.add(agileProcessHistory);
        }
        agileProcessHistoryList.add(0, startAgileProcessHistory);
        return agileProcessHistoryList;
    }

    @Override
    public AgilePage<AgileProcessTask> getUserTodoTask(AgilePageable<AgileProcessTask> agilePageable) {
        List<String> userCandidateGroupList = getUserCandidateGroupList();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(AgileSecurityContext.getUserId(), userCandidateGroupList);
        this.buildTaskInfoQuery(taskQuery, agilePageable.getQueryCond());
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
                AgileProcessTask agileProcessTask = this.buildAgileProcessTask(task);
                if (agileProcessTask != null && agileProcessTask.isNotEmptyPk()) {
                    agileProcessTask.setTaskStatus("1");
                    agileProcessTaskList.add(agileProcessTask);
                }
            });
            agileProcessTaskPage.setRecords(agileProcessTaskList);
        }

        return agileProcessTaskPage;
    }

    @Override
    public AgilePage<AgileProcessTask> getUserDoneTask(AgilePageable<AgileProcessTask> agilePageable) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(AgileSecurityContext.getUserId());
        this.buildTaskInfoQuery(historicTaskInstanceQuery, agilePageable.getQueryCond());
        long totalCount = historicTaskInstanceQuery.count();
        AgilePage<AgileProcessTask> agileProcessTaskPage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        agileProcessTaskPage.setTotal(totalCount);
        if (totalCount > 0) {
            historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc();
            if (agilePageable.getCurrentPage() < 1) {
                agilePageable.setCurrentPage(1);
            }
            int firstResult = (agilePageable.getCurrentPage() - 1) * agilePageable.getPageSize();
            int maxResults = (agilePageable.getCurrentPage()) * agilePageable.getPageSize();
            List<HistoricTaskInstance> historicTaskInstanceList = historicTaskInstanceQuery.listPage(firstResult, maxResults);
            List<AgileProcessTask> agileProcessTaskList = new ArrayList<>();
            historicTaskInstanceList.forEach(historicTaskInstance -> {
                AgileProcessTask agileProcessTask = this.buildAgileProcessTask(historicTaskInstance);
                if (agileProcessTask != null && agileProcessTask.isNotEmptyPk()) {
                    agileProcessTask.setEndTime(historicTaskInstance.getEndTime());
                    agileProcessTaskList.add(agileProcessTask);
                }
            });
            agileProcessTaskPage.setRecords(agileProcessTaskList);
        }
        return agileProcessTaskPage;
    }

    /**
     * 组装查询条件
     *
     * @param taskInfoQuery
     * @param agileProcessTask
     */
    private void buildTaskInfoQuery(TaskInfoQuery taskInfoQuery, AgileProcessTask agileProcessTask) {
        if (agileProcessTask != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getModelName())) {
                taskInfoQuery.processDefinitionNameLike("%" + agileProcessTask.getModelName() + "%");
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getModelCode())) {
                taskInfoQuery.processDefinitionKeyLike("%" + agileProcessTask.getModelCode() + "%");
            }
            if (AgileStringUtil.isNotEmpty(agileProcessTask.getStartUser())) {
                taskInfoQuery.taskOwner(agileProcessTask.getStartUser());
            }
        }
    }

    /**
     * 组装流程任务信息
     *
     * @param taskInfo
     * @return
     */
    private AgileProcessTask buildAgileProcessTask(TaskInfo taskInfo) {
        AgileProcessTask agileProcessTask = null;
        AgileProcessInstance agileProcessInstance = agileProcessInstanceService.getById(taskInfo.getProcessInstanceId());
        if (agileProcessInstance != null && agileProcessInstance.isNotEmptyPk()) {
            agileProcessTask = new AgileProcessTask();
            agileProcessTask.setId(taskInfo.getId());
            agileProcessTask.setInstanceId(agileProcessInstance.getId());
            agileProcessTask.setModelCode(agileProcessInstance.getModelCode());
            agileProcessTask.setModelName(agileProcessInstance.getModelName());
            agileProcessTask.setFormName(agileProcessInstance.getFormName());
            agileProcessTask.setTaskName(taskInfo.getName());
            agileProcessTask.setStartUser(agileProcessInstance.getStartUser());
            agileProcessTask.setStartUserName(agileProcessInstance.getStartUserName());
            agileProcessTask.setStartTime(taskInfo.getCreateTime());
        }
        return agileProcessTask;
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
        // 校验流程实例存在
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if (processInstance == null) {
            throw new AgileFrameException("流程实例已不存在！");
        }
        // 校验任务存在
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new AgileFrameException("流程任务已不存在！");
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
     * 修改流程定义激活挂起状态
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
                throw new AgileValidateException("流程定义不存在！");
            }
        } else {
            throw new AgileValidateException("流程定义ID不能为空！");
        }
    }
}
