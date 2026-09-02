package com.jeeagile.drools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.constants.AgileSwitchStatus;
import com.jeeagile.core.constants.AgileYesNo;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.drools.constants.DroolsFieldType;
import com.jeeagile.drools.constants.DroolsModelType;
import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.entity.AgileDroolsRule;
import com.jeeagile.drools.entity.AgileDroolsRuleModel;
import com.jeeagile.drools.entity.AgileDroolsSceneRule;
import com.jeeagile.drools.kie.AgileKieRule;
import com.jeeagile.drools.kie.AgileKieTemplate;
import com.jeeagile.drools.mapper.AgileDroolsRuleMapper;
import com.jeeagile.drools.util.AgileDroolsUtil;
import com.jeeagile.drools.vo.AgileDroolsModelFieldInfo;
import com.jeeagile.drools.vo.AgileDroolsModelInfo;
import com.jeeagile.drools.vo.AgileDroolsRuleInfo;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则配置 业务处理层
 */
@AgileService
public class AgileDroolsRuleServiceImpl extends AgileBaseServiceImpl<AgileDroolsRuleMapper, AgileDroolsRule> implements IAgileDroolsRuleService {
    @Autowired
    private IAgileDroolsModelService agileDroolsModelService;
    @Autowired
    private IAgileDroolsRuleModelService agileDroolsRuleModelService;
    @Autowired
    private IAgileDroolsSceneRuleService agileDroolsSceneRuleService;
    @Autowired
    private AgileKieTemplate agileKieTemplate;


    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsRule> queryWrapper(AgileDroolsRule agileDroolsRule) {
        LambdaQueryWrapper<AgileDroolsRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsRule != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleCode())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRuleCode, agileDroolsRule.getRuleCode());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleName())) {
                lambdaQueryWrapper.like(AgileDroolsRule::getRuleName, agileDroolsRule.getRuleName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleType())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRuleType, agileDroolsRule.getRuleType());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRulePackage())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRulePackage, agileDroolsRule.getRulePackage());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleStatus())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRuleStatus, agileDroolsRule.getRuleStatus());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleContent())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRuleContent, agileDroolsRule.getRuleContent());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsRule.getRuleDesc())) {
                lambdaQueryWrapper.eq(AgileDroolsRule::getRuleDesc, agileDroolsRule.getRuleDesc());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public AgileDroolsRule selectModel(Serializable ruleId) {
        AgileDroolsRule agileDroolsRule = this.getById(ruleId);
        if (agileDroolsRule != null) {
            List<String> modelIdList = agileDroolsRuleModelService.getRuleModelIdByRuleId(ruleId);
            agileDroolsRule.setModelIdList(modelIdList);
        }
        return agileDroolsRule;
    }

    @Override
    public AgileDroolsRule saveModel(AgileDroolsRule agileDroolsRule) {
        AgileValidateUtil.validateObject(agileDroolsRule);
        validateDroolsRule(agileDroolsRule);
        String ruleContent = handleDroolsRuleContent(agileDroolsRule.getRulePackage(), "", agileDroolsRule.getModelIdList());
        agileDroolsRule.setRuleContent(ruleContent);
        this.save(agileDroolsRule);
        saveDroolsRuleModel(agileDroolsRule.getId(), agileDroolsRule.getModelIdList());
        return agileDroolsRule;
    }

    @Override
    public boolean updateModel(AgileDroolsRule agileDroolsRule) {
        AgileValidateUtil.validateObject(agileDroolsRule);
        validateDroolsRule(agileDroolsRule);
        String ruleContent = handleDroolsRuleContent(agileDroolsRule.getRulePackage(), agileDroolsRule.getRuleContent(), agileDroolsRule.getModelIdList());
        agileDroolsRule.setRuleContent(ruleContent);
        this.updateDroolsRuleModel(agileDroolsRule.getId(), agileDroolsRule.getModelIdList());
        return this.updateById(agileDroolsRule);
    }

    @Override
    public boolean deleteModel(Serializable ruleId) {
        agileKieTemplate.removeContent(ruleId + ".drl");
        this.deleteDroolsRuleModel(ruleId);
        this.deleteDroolsSceneRule(ruleId);
        return this.removeById(ruleId);
    }

    /**
     * 校验规则文件
     */
    private void validateDroolsRule(AgileDroolsRule agileDroolsRule) throws AgileValidateException {
        LambdaQueryWrapper<AgileDroolsRule> queryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsRule.getId() != null) {
            queryWrapper.ne(AgileDroolsRule::getId, agileDroolsRule.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileDroolsRule::getRuleCode, agileDroolsRule.getRuleCode()).or().eq(AgileDroolsRule::getRuleName, agileDroolsRule.getRuleName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("规则文件已存在请规则文件编码和规则文件名称！");
        }
    }

    /**
     * 处理规则内容
     */
    private String handleDroolsRuleContent(String rulePackage, String ruleContent, List<String> modelIdList) {
        String packageStr = "package " + rulePackage + ";" + "\r\n";

        StringBuilder importStr = new StringBuilder();
        for (String modelId : modelIdList) {
            AgileDroolsModel agileDroolsModel = agileDroolsModelService.getById(modelId);
            StringBuilder temp = new StringBuilder();
            temp.append("import ").append(agileDroolsModel.getModelPackage()).append(".").append(agileDroolsModel.getModelName()).append(";");
            if (agileDroolsModel.isNotEmptyPk() && !ruleContent.contains(temp.toString())) {
                importStr.append(temp).append("\r\n");
            }
        }
        if (ruleContent.startsWith("package")) {
            ruleContent = ruleContent.substring(ruleContent.indexOf(";") + 1);
        }
        ruleContent.replaceFirst("\r\n", "");
        return packageStr + "\r\n" + importStr + ruleContent;
    }

    /**
     * 删除规则对象关联关系
     * @param ruleId
     * @return
     */
    private boolean deleteDroolsRuleModel(Serializable ruleId) {
        if (ruleId == null) return true;
        LambdaQueryWrapper<AgileDroolsRuleModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsRuleModel::getRuleId, ruleId);
        return agileDroolsRuleModelService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除场景规则关联关系
     * @param ruleId
     * @return
     */
    private boolean deleteDroolsSceneRule(Serializable ruleId) {
        if (ruleId == null) return true;
        LambdaQueryWrapper<AgileDroolsSceneRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsSceneRule::getRuleId, ruleId);
        return agileDroolsSceneRuleService.remove(lambdaQueryWrapper);
    }

    /**
     * 更新规则对象
     */
    private boolean updateDroolsRuleModel(String ruleId, List<String> modelIdList) {
        this.deleteDroolsRuleModel(ruleId);
        return this.saveDroolsRuleModel(ruleId, modelIdList);
    }

    /**
     * 保存规则对象
     */
    private boolean saveDroolsRuleModel(String ruleId, List<String> modelIdList) {
        if (modelIdList != null && !modelIdList.isEmpty()) {
            List<AgileDroolsRuleModel> ruleModelList = new ArrayList<>();
            for (String modelId : modelIdList) {
                AgileDroolsRuleModel droolsRuleModel = new AgileDroolsRuleModel();
                droolsRuleModel.setRuleId(ruleId);
                droolsRuleModel.setModelId(modelId);
                ruleModelList.add(droolsRuleModel);
            }
            return agileDroolsRuleModelService.saveBatch(ruleModelList);
        } else {
            return true;
        }
    }

    @Override
    public boolean changeStatus(Serializable ruleId, String ruleStatus) {
        if (!AgileSwitchStatus.isValid(ruleStatus)) {
            throw new AgileValidateException("状态值非法！");
        }
        AgileDroolsRule agileDroolsRule = this.getById(ruleId);
        if (agileDroolsRule == null || agileDroolsRule.isEmptyPk()) {
            throw new AgileValidateException("规则已不存在！");
        }
        if (AgileSwitchStatus.ENABLE.equals(ruleStatus)) {
            AgileKieRule agileKieRule = new AgileKieRule();
            agileKieRule.setContent(agileDroolsRule.getRuleContent());
            agileKieRule.setPath(agileDroolsRule.getId() + ".drl");
            agileKieTemplate.addOrUpdateKieRule(agileKieRule);
        } else {
            agileKieTemplate.removeContent(agileDroolsRule.getId() + ".drl");
        }
        LambdaUpdateWrapper<AgileDroolsRule> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileDroolsRule::getRuleStatus, ruleStatus);
        lambdaUpdateWrapper.eq(AgileDroolsRule::getId, ruleId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public AgileDroolsRuleInfo info(Serializable ruleId) {
        AgileDroolsRuleInfo agileDroolsRuleInfo = new AgileDroolsRuleInfo();
        AgileDroolsRule agileDroolsRule = this.getById(ruleId);
        if (agileDroolsRule == null || agileDroolsRule.isEmptyPk()) {
            throw new AgileValidateException("规则已不存在！");
        }
        BeanUtils.copyProperties(agileDroolsRule, agileDroolsRuleInfo);
        List<String> ruleModelIdList = agileDroolsRuleModelService.getRuleModelIdByRuleId(ruleId);
        List<AgileDroolsModelInfo> agileDroolsModelInfoList = new ArrayList<>();
        for (String modelId : ruleModelIdList) {
            AgileDroolsModel agileDroolsModel = agileDroolsModelService.getById(modelId);
            AgileDroolsModelInfo agileDroolsModelInfo = new AgileDroolsModelInfo();
            BeanUtils.copyProperties(agileDroolsModel, agileDroolsModelInfo);
            agileDroolsModelInfo.setDroolsModelFieldList(agileDroolsModelService.selectModelFieldInfoList(modelId));
            agileDroolsModelInfoList.add(agileDroolsModelInfo);
        }
        agileDroolsRuleInfo.setDroolsModelList(agileDroolsModelInfoList);
        return agileDroolsRuleInfo;
    }

    @Override
    public boolean saveRuleContent(AgileDroolsRule agileDroolsRule) {
        if (this.validateRuleContent(agileDroolsRule.getId(), agileDroolsRule.getRuleContent())) {
            LambdaUpdateWrapper<AgileDroolsRule> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(AgileDroolsRule::getRuleContent, agileDroolsRule.getRuleContent());
            lambdaUpdateWrapper.eq(AgileDroolsRule::getId, agileDroolsRule.getId());
            this.update(lambdaUpdateWrapper);
            // 如果状态已被启用 验证完成后将内容回复
            if (AgileSwitchStatus.ENABLE.equals(agileDroolsRule.getRuleStatus())) {
                AgileKieRule agileKieRule = new AgileKieRule();
                agileKieRule.setPath(agileDroolsRule.getId() + ".drl");
                agileKieRule.setContent(agileDroolsRule.getRuleContent());
                agileKieTemplate.addContent(agileKieRule);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean validateRuleContent(String ruleId, String ruleContent) {
        AgileDroolsRule agileDroolsRule = this.getById(ruleId);
        if (agileDroolsRule == null || agileDroolsRule.isEmptyPk()) {
            throw new AgileValidateException("规则已不存在！");
        }
        if (!ruleContent.contains(agileDroolsRule.getRulePackage())) {
            throw new AgileValidateException("规则内容package与规则信息package不一致！");
        }
        AgileKieRule agileKieRule = new AgileKieRule();
        agileKieRule.setPath(ruleId + ".drl");
        agileKieRule.setContent(ruleContent);
        Results results = agileKieTemplate.verify(agileKieRule);
        if (results.hasMessages(Message.Level.ERROR)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Message message : results.getMessages(Message.Level.ERROR)) {
                stringBuilder.append(message.getId()).append("、").append(message.getText()).append("\r\n");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            throw new AgileValidateException(stringBuilder.toString());
        }
        // 如果状态已被启用 验证完成后将内容恢复
        if (AgileSwitchStatus.ENABLE.equals(agileDroolsRule.getRuleStatus())) {
            agileKieRule.setContent(agileDroolsRule.getRuleContent());
            agileKieTemplate.addOrUpdateKieRule(agileKieRule);
        }
        return true;
    }

    @Override
    public Object test(String ruleId, String ruleContent, Map paramData) {
        try {
            KieHelper kieHelper = new KieHelper();
            List<AgileDroolsRuleModel> ruleModelList = agileDroolsRuleModelService.selectRuleModelListByRuleId(ruleId);
            List<AgileDroolsModel> droolsModelList = new ArrayList<>();
            Map<String, List<AgileDroolsModelFieldInfo>> modelFieldInfoMap = new HashMap<>();
            for (AgileDroolsRuleModel agileDroolsRuleModel : ruleModelList) {
                AgileDroolsModel agileDroolsModel = agileDroolsModelService.getById(agileDroolsRuleModel.getModelId());
                List<AgileDroolsModelFieldInfo> modelFieldInfoList = agileDroolsModelService.selectModelFieldInfoList(agileDroolsRuleModel.getModelId());
                modelFieldInfoMap.put(agileDroolsRuleModel.getModelId(), modelFieldInfoList);
                this.addModelContent(kieHelper, agileDroolsModel, modelFieldInfoList);
                droolsModelList.add(agileDroolsModel);
            }
            kieHelper.addContent(ruleContent, ruleId + ".drl");
            KieBase kieBase = kieHelper.build();
            KieSession kieSession = kieBase.newKieSession();
            Map<String, Object> rtnData = new HashMap<>();
            for (AgileDroolsModel agileDroolsModel : droolsModelList) {
                Object object = null;
                if (AgileYesNo.YES.equals(agileDroolsModel.getInputFlag())) {
                    if (DroolsModelType.DECLARE.equals(agileDroolsModel.getModelType())) {
                        FactType factType = kieBase.getFactType(agileDroolsModel.getModelPackage(), agileDroolsModel.getModelName());
                        object = factType.newInstance();
                        List<AgileDroolsModelFieldInfo> modelFieldInfoList = modelFieldInfoMap.get(agileDroolsModel.getId());
                        if (AgileStringUtil.isNotEmpty(paramData.get(agileDroolsModel.getModelName()))) {
                            Map data = (Map) paramData.get(agileDroolsModel.getModelName());
                            for (AgileDroolsModelFieldInfo modelFieldInfo : modelFieldInfoList) {
                                AgileDroolsUtil.handlerParamData(kieBase, factType, object, data, modelFieldInfo);
                            }
                        }
                        kieSession.insert(object);
                    } else {
                        object = Class.forName(agileDroolsModel.getModelPackage() + "." + agileDroolsModel.getModelName());
                        if (AgileStringUtil.isNotEmpty(paramData.get(agileDroolsModel.getModelName()))) {
                            BeanUtils.copyProperties(paramData.get(agileDroolsModel.getModelName()), object);
                        }
                        kieSession.insert(object);
                    }
                }
                if (AgileYesNo.YES.equals(agileDroolsModel.getOutputFlag()) && object != null) {
                    rtnData.put(agileDroolsModel.getModelName(), object);
                }
            }
            kieSession.fireAllRules();
            return rtnData;
        } catch (Exception e) {
            throw new AgileFrameException("规则测试异常：" + e.getMessage());
        }
    }

    private void addModelContent(KieHelper kieHelper, AgileDroolsModel agileDroolsModel, List<AgileDroolsModelFieldInfo> modelFieldInfoList) {
        String modelSourceCode = AgileDroolsUtil.getModelSourceCode(agileDroolsModel, modelFieldInfoList);
        if (DroolsModelType.JAVA.equals(agileDroolsModel.getModelType())) {
            kieHelper.addContent(modelSourceCode, ResourceType.JAVA);
        } else {
            kieHelper.addContent(modelSourceCode, agileDroolsModel.getId() + ".drl");
        }
        for (AgileDroolsModelFieldInfo droolsModelFieldInfo : modelFieldInfoList) {
            if (DroolsFieldType.Object.equals(droolsModelFieldInfo.getFieldType())) {
                this.addModelContent(kieHelper, droolsModelFieldInfo.getDroolsModelInfo(), droolsModelFieldInfo.getDroolsModelInfo().getDroolsModelFieldList());
            }
        }
    }
}
