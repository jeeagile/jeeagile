package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.frame.constants.online.OnlinePageType;
import com.jeeagile.frame.entity.online.AgileOnlinePage;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.online.IAgileOnlinePageService;
import com.jeeagile.frame.service.system.IAgileSysDeptService;
import com.jeeagile.frame.service.system.IAgileSysPostService;
import com.jeeagile.frame.service.system.IAgileSysRoleService;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.frame.util.AgileBeanUtils;
import com.jeeagile.process.constants.ProcessDeploymentStatus;
import com.jeeagile.process.constants.ProcessFormType;
import com.jeeagile.process.entity.*;
import com.jeeagile.process.mapper.AgileProcessDesignerMapper;
import com.jeeagile.process.support.IAgileProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author JeeAgile
 * @date 2022-06-07
 * @description 流程设计
 */
@AgileService
public class AgileProcessDesignerServiceImpl extends AgileBaseServiceImpl<AgileProcessDesignerMapper, AgileProcessDesigner> implements IAgileProcessDesignerService {

    @Autowired
    private IAgileProcessService agileProcessService;
    @Autowired
    private IAgileProcessFormService agileProcessFormService;
    @Autowired
    private IAgileProcessDefinitionService agileProcessDefinitionService;
    @Autowired
    private IAgileOnlinePageService agileOnlinePageService;
    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysPostService agileSysPostService;
    @Autowired
    private IAgileProcessExpressionService agileProcessExpressionService;

    @Override
    public LambdaQueryWrapper<AgileProcessDesigner> queryWrapper(AgileProcessDesigner agileProcessDesigner) {
        LambdaQueryWrapper<AgileProcessDesigner> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessDesigner.class, i -> !"processXml".contains(i.getProperty()));
        if (agileProcessDesigner != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessDesigner.getProcessCode())) {
                lambdaQueryWrapper.eq(AgileProcessDesigner::getProcessCode, agileProcessDesigner.getProcessCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDesigner.getProcessName())) {
                lambdaQueryWrapper.like(AgileProcessDesigner::getProcessName, agileProcessDesigner.getProcessName());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessDesigner.getDeploymentStatus())) {
                lambdaQueryWrapper.eq(AgileProcessDesigner::getDeploymentStatus, agileProcessDesigner.getDeploymentStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileProcessDesigner agileProcessDesigner) {
        handlerDeploymentStatus(agileProcessDesigner);
        this.validateProcessForm(agileProcessDesigner);
        this.validateProcessModel(agileProcessDesigner);
    }

    @Override
    public void updateModelValidate(AgileProcessDesigner agileProcessDesigner) {
        handlerDeploymentStatus(agileProcessDesigner);
        agileProcessDesigner.setDeploymentTime(null);
        this.validateProcessForm(agileProcessDesigner);
        this.validateProcessModel(agileProcessDesigner);
    }

    /**
     * 校验流程表单配置
     *
     * @param agileProcessDesigner
     */
    private void validateProcessForm(AgileProcessDesigner agileProcessDesigner) {
        if (AgileStringUtil.isEmpty(agileProcessDesigner.getFormType())) {
            throw new AgileValidateException("请选择表单类型！");
        }
        if (agileProcessDesigner.getFormType().equals(ProcessFormType.PROCESS_FORM)) {
            if (AgileStringUtil.isNotEmpty(agileProcessDesigner.getFormId())) {
                agileProcessDesigner.setFormUrl("");
            } else {
                throw new AgileValidateException("请选择流程表单！");
            }
        }

        if (agileProcessDesigner.getFormType().equals(ProcessFormType.BUSINESS_FORM)) {
            if (AgileStringUtil.isNotEmpty(agileProcessDesigner.getFormUrl())) {
                agileProcessDesigner.setFormId("");
            } else {
                throw new AgileValidateException("请选择填写表单地址！");
            }
        }
    }

    /**
     * 校验流程流程名称和流程编码是否存在
     *
     * @param agileProcessDesigner
     */
    private void validateProcessModel(AgileProcessDesigner agileProcessDesigner) {
        LambdaQueryWrapper<AgileProcessDesigner> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileProcessDesigner.getId() != null) {
            lambdaQueryWrapper.ne(AgileProcessDesigner::getId, agileProcessDesigner.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileProcessDesigner::getProcessCode, agileProcessDesigner.getProcessCode()).or().eq(AgileProcessDesigner::getProcessName, agileProcessDesigner.getProcessName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("流程名称或流程编码已存在！");
        }
    }

    @Override
    public AgileProcessDesigner saveProcessXml(String processId, String processXml) {
        AgileProcessDesigner agileProcessDesigner = this.getById(processId);
        if (agileProcessDesigner == null || agileProcessDesigner.isEmptyPk()) {
            throw new AgileValidateException("流程模型已不存在！");
        }
        agileProcessDesigner.setProcessXml(processXml);
        handlerDeploymentStatus(agileProcessDesigner);
        this.updateById(agileProcessDesigner);
        return agileProcessDesigner;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processDeployment(String processId) {
        AgileProcessDesigner agileProcessDesigner = this.getById(processId);
        if (agileProcessDesigner != null && agileProcessDesigner.isNotEmptyPk()) {
            if (ProcessDeploymentStatus.PUBLISHED.equals(agileProcessDesigner.getDeploymentStatus())) {
                throw new AgileFrameException("当前流程已处于发布状态！");
            }
            if (AgileStringUtil.isEmpty(agileProcessDesigner.getProcessXml())) {
                throw new AgileFrameException("请先进行流程设计！");
            }
            AgileProcessForm agileProcessForm = null;
            if (agileProcessDesigner.getFormType().equals(ProcessFormType.PROCESS_FORM)) {
                agileProcessForm = agileProcessFormService.getById(agileProcessDesigner.getFormId());
                if (agileProcessForm == null || agileProcessForm.isEmptyPk()) {
                    throw new AgileFrameException("流程表单不存在，请核实！");
                }
                if ("1".equals(agileProcessForm.getFormStatus())) {
                    throw new AgileFrameException("流程表单已停用！");
                }
            }
            //流程发布
            String deploymentId = agileProcessService.processDeployment(agileProcessDesigner);
            String definitionId = agileProcessService.getProcessDefinitionId(deploymentId);
            agileProcessDesigner.setDeploymentStatus(ProcessDeploymentStatus.PUBLISHED);
            agileProcessDesigner.setDeploymentTime(new Date());
            AgileProcessDefinition agileProcessDefinition = new AgileProcessDefinition();
            AgileBeanUtils.copyProperties(agileProcessDesigner, agileProcessDefinition);
            if (agileProcessDesigner.getFormType().equals(ProcessFormType.PROCESS_FORM)) {
                agileProcessDefinition.setFormName(agileProcessForm.getFormName());
                agileProcessDefinition.setFormConf(agileProcessForm.getFormConf());
                agileProcessDefinition.setFormFields(agileProcessForm.getFormFields());
            }
            agileProcessDefinition.setSuspensionState(1);
            agileProcessDefinition.setId(definitionId);
            agileProcessDefinition.setProcessId(agileProcessDesigner.getId());
            agileProcessDefinitionService.saveModel(agileProcessDefinition);

            return this.updateById(agileProcessDesigner);
        } else {
            throw new AgileFrameException("流程已不存在无法进行发布操作！");
        }
    }


    /**
     * 处理发布状态 如果已处于发布状态则修改状态为未发布，且将版本号加一
     */
    private synchronized void handlerDeploymentStatus(AgileProcessDesigner agileProcessDesigner) {
        String deploymentStatus = agileProcessDesigner.getDeploymentStatus();
        if (AgileStringUtil.isNotEmpty(deploymentStatus) && deploymentStatus.equals(ProcessDeploymentStatus.PUBLISHED)) {
            agileProcessDesigner.setDeploymentStatus(ProcessDeploymentStatus.UNPUBLISHED);
            agileProcessDesigner.setDeploymentTime(null);
            agileProcessDesigner.setProcessVersion(agileProcessDesigner.getProcessVersion() + 1);
        } else {
            agileProcessDesigner.setDeploymentStatus(ProcessDeploymentStatus.UNPUBLISHED);
            agileProcessDesigner.setDeploymentTime(null);
        }
    }

    @Override
    public Map<String, Object> selectProcessOnlinePageList() {
        Map rtnMap = new HashMap();
        LambdaQueryWrapper<AgileProcessDesigner> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessDesigner::getId, AgileProcessDesigner::getFormId, AgileProcessDesigner::getProcessName);
        lambdaQueryWrapper.eq(AgileProcessDesigner::getDeploymentStatus, ProcessDeploymentStatus.PUBLISHED);
        lambdaQueryWrapper.eq(AgileProcessDesigner::getFormType, ProcessFormType.ONLINE_FORM);
        List<AgileProcessDesigner> agileProcessDesignerList = this.list(lambdaQueryWrapper);
        List<AgileOnlinePage> agileOnlinePageList = new ArrayList<>();
        agileProcessDesignerList.forEach(agileProcessModel -> {
            LambdaQueryWrapper<AgileOnlinePage> pageQueryWrapper = new LambdaQueryWrapper<>();
            pageQueryWrapper.select(AgileOnlinePage::getId, AgileOnlinePage::getFormId, AgileOnlinePage::getPageName, AgileOnlinePage::getPageType);
            pageQueryWrapper.eq(AgileOnlinePage::getFormId, agileProcessModel.getFormId());
            pageQueryWrapper.in(AgileOnlinePage::getPageType, OnlinePageType.ORDER);
            agileOnlinePageList.addAll(this.agileOnlinePageService.list(pageQueryWrapper));
        });
        rtnMap.put("processList", agileProcessDesignerList);
        rtnMap.put("orderPageList", agileOnlinePageList);
        return rtnMap;
    }

    @Override
    public AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable) {
        AgilePage<AgileSysUser> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysUser::getId, AgileSysUser::getUserName, AgileSysUser::getNickName);
        AgileSysUser agileSysUser = agilePageable.getQueryCond();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                lambdaQueryWrapper.like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysUser::getUserStatus, "0");
        lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
        lambdaQueryWrapper.orderByAsc(AgileSysUser::getUserSort);
        return agileSysUserService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysRole> selectRolePage(AgilePageable<AgileSysRole> agilePageable) {
        AgilePage<AgileSysRole> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysRole::getId, AgileSysRole::getRoleCode, AgileSysRole::getRoleName);
        AgileSysRole agileSysRole = agilePageable.getQueryCond();
        if (agileSysRole != null) {
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleCode())) {
                lambdaQueryWrapper.eq(AgileSysRole::getRoleCode, agileSysRole.getRoleCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleName())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleName, agileSysRole.getRoleName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysRole::getRoleStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysRole::getRoleSort);
        return agileSysRoleService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysDept> selectDeptPage(AgilePageable<AgileSysDept> agilePageable) {
        AgilePage<AgileSysDept> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysDept::getId, AgileSysDept::getDeptCode, AgileSysDept::getDeptName);
        AgileSysDept agileSysDept = agilePageable.getQueryCond();
        if (agileSysDept != null) {
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptCode())) {
                lambdaQueryWrapper.eq(AgileSysDept::getDeptCode, agileSysDept.getDeptCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptName())) {
                lambdaQueryWrapper.like(AgileSysDept::getDeptName, agileSysDept.getDeptName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysDept::getDeptStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysDept::getParentId, AgileSysDept::getDeptSort);
        return agileSysDeptService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysPost> selectPostPage(AgilePageable<AgileSysPost> agilePageable) {
        AgilePage<AgileSysPost> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysPost::getId, AgileSysPost::getPostCode, AgileSysPost::getPostName);
        AgileSysPost agileSysPost = agilePageable.getQueryCond();
        if (agileSysPost != null) {
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostCode())) {
                lambdaQueryWrapper.eq(AgileSysPost::getPostCode, agileSysPost.getPostCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostName())) {
                lambdaQueryWrapper.like(AgileSysPost::getPostName, agileSysPost.getPostName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysPost::getPostStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysPost::getPostSort);
        return agileSysPostService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysGroup> selectGroupPage(AgilePageable<AgileSysGroup> agilePageable) {
        return null;
    }

    @Override
    public AgilePage<AgileProcessExpression> selectExpressionPage(AgilePageable<AgileProcessExpression> agilePageable) {
        AgilePage<AgileProcessExpression> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileProcessExpression> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileProcessExpression::getId, AgileProcessExpression::getExpressionCode, AgileProcessExpression::getExpressionName, AgileProcessExpression::getExpressionValue);
        AgileProcessExpression agileProcessExpression = agilePageable.getQueryCond();
        if (agileProcessExpression != null) {
            if (AgileStringUtil.isNotEmpty(agileProcessExpression.getExpressionCode())) {
                lambdaQueryWrapper.eq(AgileProcessExpression::getExpressionCode, agileProcessExpression.getExpressionCode());
            }
            if (AgileStringUtil.isNotEmpty(agileProcessExpression.getExpressionName())) {
                lambdaQueryWrapper.like(AgileProcessExpression::getExpressionName, agileProcessExpression.getExpressionName());
            }
        }
        lambdaQueryWrapper.eq(AgileProcessExpression::getExpressionStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileProcessExpression::getExpressionCode);
        return agileProcessExpressionService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileProcessScript> selectScriptPage(AgilePageable<AgileProcessScript> agilePageable) {
        return null;
    }

    @Override
    public List<String> detailUserNickName(List<String> userIds) {
        List<String> nickNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(userIds)) {
            userIds.forEach(id -> {
                AgileSysUser agileSysUser = agileSysUserService.getById(id);
                if (agileSysUser != null && agileSysUser.isNotEmptyPk()) {
                    nickNameList.add(agileSysUser.getNickName());
                }
            });
        }
        return nickNameList;
    }

    @Override
    public List<String> detailRoleName(List<String> roleIds) {
        List<String> roleNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(roleIds)) {
            roleIds.forEach(id -> {
                AgileSysRole agileSysRole = agileSysRoleService.getById(id);
                if (agileSysRole != null && agileSysRole.isNotEmptyPk()) {
                    roleNameList.add(agileSysRole.getRoleName());
                }
            });
        }
        return roleNameList;
    }

    @Override
    public List<String> detailDeptName(List<String> deptIds) {
        List<String> deptNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(deptIds)) {
            deptIds.forEach(id -> {
                AgileSysDept agileSysDept = agileSysDeptService.getById(id);
                if (agileSysDept != null && agileSysDept.isNotEmptyPk()) {
                    deptNameList.add(agileSysDept.getDeptName());
                }
            });
        }
        return deptNameList;
    }

    @Override
    public List<String> detailPostName(List<String> postIds) {
        List<String> postNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(postIds)) {
            postIds.forEach(id -> {
                AgileSysPost agileSysPost = agileSysPostService.getById(id);
                if (agileSysPost != null && agileSysPost.isNotEmptyPk()) {
                    postNameList.add(agileSysPost.getPostName());
                }
            });
        }
        return postNameList;
    }

    @Override
    public List<String> detailGroupName(List<String> groupCodes) {
        return null;
    }

    @Override
    public List<String> detailScriptName(List<String> scriptCodes) {
        return null;
    }
}
