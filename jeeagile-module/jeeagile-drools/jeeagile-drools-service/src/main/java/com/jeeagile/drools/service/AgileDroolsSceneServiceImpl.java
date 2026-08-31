package com.jeeagile.drools.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.constants.AgileSuccessFail;
import com.jeeagile.core.constants.AgileSwitchStatus;
import com.jeeagile.core.constants.AgileYesNo;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileExceptionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.drools.constants.DroolsModelType;
import com.jeeagile.drools.entity.*;
import com.jeeagile.drools.entity.AgileDroolsScene;
import com.jeeagile.drools.kie.AgileKieBase;
import com.jeeagile.drools.kie.AgileKieRule;
import com.jeeagile.drools.kie.AgileKieTemplate;
import com.jeeagile.drools.listener.AgileDroolsAgendaEventListener;
import com.jeeagile.drools.mapper.AgileDroolsSceneMapper;
import com.jeeagile.drools.util.AgileDroolsUtil;
import com.jeeagile.drools.vo.AgileDroolsModelFieldInfo;
import com.jeeagile.drools.vo.AgileDroolsModelInfo;
import com.jeeagile.drools.vo.AgileDroolsSceneInfo;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.thoughtworks.xstream.core.BaseException;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 规则场景 业务处理层
 */
@AgileService
public class AgileDroolsSceneServiceImpl extends AgileBaseServiceImpl<AgileDroolsSceneMapper, AgileDroolsScene> implements IAgileDroolsSceneService {

    @Autowired
    private IAgileDroolsSceneRuleService agileDroolsSceneRuleService;
    @Autowired
    private IAgileDroolsModelService agileDroolsModelService;
    @Autowired
    private IAgileDroolsSceneLoggerService agileDroolsSceneLoggerService;
    @Autowired
    private IAgileDroolsRuleLoggerService agileDroolsRuleLoggerService;
    @Autowired
    private IAgileDroolsRuleService agileDroolsRuleService;
    @Autowired
    private AgileKieTemplate agileKieTemplate;

    /**
     * 拼装查询条件
     */
    @Override
    public LambdaQueryWrapper<AgileDroolsScene> queryWrapper(AgileDroolsScene agileDroolsScene) {
        LambdaQueryWrapper<AgileDroolsScene> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsScene != null) {
            if (AgileStringUtil.isNotEmpty(agileDroolsScene.getSceneCode())) {
                lambdaQueryWrapper.eq(AgileDroolsScene::getSceneCode, agileDroolsScene.getSceneCode());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsScene.getSceneName())) {
                lambdaQueryWrapper.like(AgileDroolsScene::getSceneName, agileDroolsScene.getSceneName());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsScene.getSceneStatus())) {
                lambdaQueryWrapper.eq(AgileDroolsScene::getSceneStatus, agileDroolsScene.getSceneStatus());
            }
            if (AgileStringUtil.isNotEmpty(agileDroolsScene.getSceneDesc())) {
                lambdaQueryWrapper.eq(AgileDroolsScene::getSceneDesc, agileDroolsScene.getSceneDesc());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public AgileDroolsScene selectModel(Serializable ruleId) {
        AgileDroolsScene agileDroolsScene = this.getById(ruleId);
        if (agileDroolsScene != null) {
            List<String> ruleIdList = agileDroolsSceneRuleService.getSceneRuleIdBySceneId(ruleId);
            agileDroolsScene.setRuleIdList(ruleIdList);
        }
        return agileDroolsScene;
    }

    @Override
    public AgileDroolsScene saveModel(AgileDroolsScene agileDroolsScene) {
        AgileValidateUtil.validateObject(agileDroolsScene);
        validateDroolsScene(agileDroolsScene);
        this.save(agileDroolsScene);
        this.saveDroolsSceneRule(agileDroolsScene.getId(), agileDroolsScene.getRuleIdList());
        return agileDroolsScene;
    }

    @Override
    public boolean updateModel(AgileDroolsScene agileDroolsScene) {
        AgileValidateUtil.validateObject(agileDroolsScene);
        validateDroolsScene(agileDroolsScene);
        this.updateDroolsSceneRule(agileDroolsScene.getId(), agileDroolsScene.getRuleIdList());
        return this.updateById(agileDroolsScene);
    }

    @Override
    public boolean deleteModel(Serializable sceneId) {
        this.deleteDroolsSceneRule(sceneId);
        return this.removeById(sceneId);
    }

    @Override
    public boolean changeStatus(String sceneId, String sceneStatus) {
        if (!AgileSwitchStatus.isValid(sceneStatus)) {
            throw new AgileValidateException("状态值非法！");
        }
        AgileDroolsScene agileDroolsScene = this.getById(sceneId);
        if (agileDroolsScene == null || agileDroolsScene.isEmptyPk()) {
            throw new AgileValidateException("规则场景已不存在！");
        }
        if (AgileSwitchStatus.ENABLE.equals(sceneStatus)) {
            agileKieTemplate.addOrUpdateKieBaseModel(handleAgileKieBase(agileDroolsScene));
            agileKieTemplate.updateKieContainer();
        }
        LambdaUpdateWrapper<AgileDroolsScene> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileDroolsScene::getSceneStatus, sceneStatus);
        lambdaUpdateWrapper.eq(AgileDroolsScene::getId, sceneId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public AgileDroolsSceneInfo info(Serializable sceneId) {
        AgileDroolsScene agileDroolsScene = this.getById(sceneId);
        if (agileDroolsScene == null || agileDroolsScene.isEmptyPk()) {
            throw new AgileValidateException("规则场景已不存在！");
        }
        AgileDroolsSceneInfo droolsSceneInfo = new AgileDroolsSceneInfo();
        BeanUtils.copyProperties(agileDroolsScene, droolsSceneInfo);
        List<AgileDroolsRule> droolsRuleList = this.baseMapper.getDroolsSceneRuleList((String) sceneId);
        droolsSceneInfo.setDroolsRuleList(droolsRuleList);
        List<AgileDroolsModel> droolsModelList = this.baseMapper.getDroolsSceneRuleModelList((String) sceneId);
        if (AgileStringUtil.isNotEmpty(droolsModelList)) {
            List<AgileDroolsModelInfo> droolsModelInfoList = new ArrayList<>();
            for (AgileDroolsModel agileDroolsModel : droolsModelList) {
                List<AgileDroolsModelFieldInfo> droolsModelFieldInfoList = agileDroolsModelService.selectModelFieldInfoList(agileDroolsModel.getId());
                AgileDroolsModelInfo droolsModelInfo = new AgileDroolsModelInfo();
                BeanUtils.copyProperties(agileDroolsModel, droolsModelInfo);
                droolsModelInfo.setDroolsModelFieldList(droolsModelFieldInfoList);
                droolsModelInfoList.add(droolsModelInfo);
            }
            droolsSceneInfo.setDroolsModelList(droolsModelInfoList);
        }
        return droolsSceneInfo;
    }

    @Override
    public Object execute(String sceneCode, Map paramData) {
        AgileDroolsSceneLogger agileDroolsSceneLogger = new AgileDroolsSceneLogger();
        agileDroolsSceneLogger.setId(AgileStringUtil.getUuid());
        agileDroolsSceneLogger.setStartTime(new Date());
        AgileDroolsAgendaEventListener agendaEventListener = new AgileDroolsAgendaEventListener(agileDroolsSceneLogger.getId());
        try {
            LambdaQueryWrapper<AgileDroolsScene> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileDroolsScene::getSceneCode, sceneCode);
            AgileDroolsScene agileDroolsScene = this.getOne(lambdaQueryWrapper);
            if (agileDroolsScene == null || agileDroolsScene.isEmptyPk() || !AgileSwitchStatus.ENABLE.equals(agileDroolsScene.getSceneStatus())) {
                throw new AgileValidateException("规则场景已不存在或未启用！");
            }
            agileDroolsSceneLogger.setSceneId(agileDroolsScene.getId());
            agileDroolsSceneLogger.setSceneCode(agileDroolsScene.getSceneCode());
            agileDroolsSceneLogger.setSceneName(agileDroolsScene.getSceneName());
            KieBase kieBase = agileKieTemplate.getKieBase(sceneCode);
            KieSession kieSession = agileKieTemplate.getKieSession(sceneCode);
            List<AgileDroolsModel> droolsModelList = this.baseMapper.getDroolsSceneRuleModelList(agileDroolsScene.getId());
            Map rtnData = new HashMap();
            for (AgileDroolsModel agileDroolsModel : droolsModelList) {
                Object object = null;
                if (AgileYesNo.YES.equals(agileDroolsModel.getInputFlag())) {
                    if (DroolsModelType.DECLARE.equals(agileDroolsModel.getModelType())) {
                        List<AgileDroolsModelFieldInfo> modelFieldInfoList = agileDroolsModelService.selectModelFieldInfoList(agileDroolsModel.getId());
                        FactType factType = kieBase.getFactType(agileDroolsModel.getModelPackage(), agileDroolsModel.getModelName());
                        object = factType.newInstance();
                        if (AgileStringUtil.isNotEmpty(paramData.get(agileDroolsModel.getModelName()))) {
                            Map data = (Map) paramData.get(agileDroolsModel.getModelName());
                            for (AgileDroolsModelFieldInfo modelFieldInfo : modelFieldInfoList) {
                                AgileDroolsUtil.handlerParamData(kieBase, factType, object, data, modelFieldInfo);
                            }
                        }
                        kieSession.insert(object);
                        if (AgileYesNo.YES.equals(agileDroolsModel.getOutputFlag()) && object != null) {
                            rtnData.put(agileDroolsModel.getModelName(), object);
                        }
                    }
                }
            }
            kieSession.addEventListener(agendaEventListener);
            int count = kieSession.fireAllRules();
            kieSession.dispose();
            if (AgileStringUtil.isNotEmpty(paramData)) {
                agileDroolsSceneLogger.setExecuteParam(JSONObject.toJSONString(paramData));
            }
            if (AgileStringUtil.isNotEmpty(rtnData)) {
                agileDroolsSceneLogger.setExecuteResult(JSONObject.toJSONString(rtnData));
            }
            agileDroolsSceneLogger.setRuleCount(count);
            agileDroolsSceneLogger.setExecuteStatus(AgileSuccessFail.SUCCESS);
            return rtnData;
        } catch (Exception ex) {
            agileDroolsSceneLogger.setExecuteStatus(AgileSuccessFail.FAIL);
            String errorMsg = null;
            if (ex instanceof AgileBaseException) {
                errorMsg = ex.getMessage();
            } else {
                errorMsg = AgileExceptionUtil.stacktraceToString(ex);
            }
            agileDroolsSceneLogger.setErrorMsg(errorMsg);
            throw new AgileFrameException("场景规则执行异常：" + ex.getMessage());
        } finally {
            agileDroolsSceneLogger.setEndTime(new Date());
            long executeTime = agileDroolsSceneLogger.getEndTime().getTime() - agileDroolsSceneLogger.getStartTime().getTime();
            agileDroolsSceneLogger.setExecuteTime(executeTime);
            List<AgileDroolsRuleLogger> loggerRuleList = new ArrayList<>();
            if (AgileStringUtil.isNotEmpty(agendaEventListener.getLoggerRuleMap())) {
                loggerRuleList.addAll(agendaEventListener.getLoggerRuleMap().values());
            }
            this.asyncSaveAgileDroolsLogger(agileDroolsSceneLogger, loggerRuleList);
        }
    }

    /**
     * 校验规则文件
     */
    private void validateDroolsScene(AgileDroolsScene agileDroolsScene) throws AgileValidateException {
        LambdaQueryWrapper<AgileDroolsScene> queryWrapper = new LambdaQueryWrapper<>();
        if (agileDroolsScene.getId() != null) {
            queryWrapper.ne(AgileDroolsScene::getId, agileDroolsScene.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileDroolsScene::getSceneCode, agileDroolsScene.getSceneCode()).or().eq(AgileDroolsScene::getSceneName, agileDroolsScene.getSceneName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("规则场景已存在请场景编码或场景名称！");
        }
    }

    private boolean deleteDroolsSceneRule(Serializable sceneId) {
        if (sceneId == null) return true;
        LambdaQueryWrapper<AgileDroolsSceneRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileDroolsSceneRule::getRuleId, sceneId);
        return agileDroolsSceneRuleService.remove(lambdaQueryWrapper);
    }

    /**
     * 更新规则对象
     */
    private boolean updateDroolsSceneRule(String sceneId, List<String> ruleIdList) {
        this.deleteDroolsSceneRule(sceneId);
        return this.saveDroolsSceneRule(sceneId, ruleIdList);
    }

    /**
     * 保存规则对象
     */
    private boolean saveDroolsSceneRule(String sceneId, List<String> ruleIdList) {
        if (ruleIdList != null && !ruleIdList.isEmpty()) {
            List<AgileDroolsSceneRule> sceneRuleList = new ArrayList<>();
            for (String ruleId : ruleIdList) {
                AgileDroolsSceneRule droolsSceneRule = new AgileDroolsSceneRule();
                droolsSceneRule.setRuleId(ruleId);
                droolsSceneRule.setSceneId(sceneId);
                sceneRuleList.add(droolsSceneRule);
            }
            return agileDroolsSceneRuleService.saveBatch(sceneRuleList);
        } else {
            return true;
        }
    }

    /**
     * 组装AgileKieBase
     *
     * @param agileDroolsScene
     * @return
     */
    private AgileKieBase handleAgileKieBase(AgileDroolsScene agileDroolsScene) {
        AgileKieBase agileKieBase = new AgileKieBase();
        agileKieBase.setName(agileDroolsScene.getSceneCode());
        List<AgileDroolsRule> droolsSceneList = this.baseMapper.getDroolsSceneRuleList(agileDroolsScene.getId());
        List<String> packageList = new ArrayList<>();
        for (AgileDroolsRule agileDroolsRule : droolsSceneList) {
            packageList.add(agileDroolsRule.getRulePackage());
        }
        List<AgileDroolsModel> droolsModelList = this.baseMapper.getDroolsSceneRuleModelList(agileDroolsScene.getId());
        for (AgileDroolsModel agileDroolsModel : droolsModelList) {
            packageList.add(agileDroolsModel.getModelPackage());
        }
        List<AgileDroolsModel> modelFieldObjectList = this.baseMapper.getDroolsModelFieldObjectList(agileDroolsScene.getId());
        for (AgileDroolsModel agileDroolsModel : modelFieldObjectList) {
            packageList.add(agileDroolsModel.getModelPackage());
        }
        agileKieBase.setPackages(packageList);
        return agileKieBase;
    }

    @Async()
    public void asyncSaveAgileDroolsLogger(AgileDroolsSceneLogger agileDroolsSceneLogger, List<AgileDroolsRuleLogger> ruleLoggerList) {
        agileDroolsSceneLoggerService.saveModel(agileDroolsSceneLogger);
        if (AgileStringUtil.isNotEmpty(ruleLoggerList)) {
            agileDroolsRuleLoggerService.saveBatch(ruleLoggerList);
        }

    }

    @PostConstruct
    public void initDroolsScene() {
        try {
            this.initDroolsModel();
            this.initDroolsRule();
            LambdaQueryWrapper<AgileDroolsScene> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileDroolsScene::getSceneStatus, AgileSwitchStatus.ENABLE);
            List<AgileDroolsScene> droolsSceneList = this.list(lambdaQueryWrapper);
            for (AgileDroolsScene agileDroolsScene : droolsSceneList) {
                agileKieTemplate.addKieBaseModel(handleAgileKieBase(agileDroolsScene));
            }
            agileKieTemplate.updateKieContainer();
        } catch (Exception e) {
            logger.error("初始化加载规则场景发生异常！", e);
        }
    }

    /**
     * 初始化规则
     */
    public void initDroolsRule() {
        try {
            LambdaQueryWrapper<AgileDroolsRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileDroolsRule::getRuleStatus, AgileSwitchStatus.ENABLE);
            List<AgileDroolsRule> droolsRuleList = agileDroolsRuleService.list(lambdaQueryWrapper);
            for (AgileDroolsRule agileDroolsRule : droolsRuleList) {
                AgileKieRule agileKieRule = new AgileKieRule();
                agileKieRule.setContent(agileDroolsRule.getRuleContent());
                agileKieRule.setPath(agileDroolsRule.getId() + ".drl");
                agileKieTemplate.addContent(agileKieRule);
            }
        } catch (Exception e) {
            logger.error("初始化加载规则发生异常！", e);
        }
    }

    /**
     * 初始化加载数据对象
     */
    public void initDroolsModel() {
        try {
            LambdaQueryWrapper<AgileDroolsModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileDroolsModel::getModelStatus, AgileSwitchStatus.ENABLE);
            List<AgileDroolsModel> droolsModelList = agileDroolsModelService.list(lambdaQueryWrapper);
            for (AgileDroolsModel agileDroolsModel : droolsModelList) {
                String sourceCode = AgileDroolsUtil.getModelSourceCode(agileDroolsModel, agileDroolsModelService.selectModelFieldInfoList(agileDroolsModel.getId()));
                AgileKieRule agileKieRule = new AgileKieRule();
                agileKieRule.setContent(sourceCode);
                agileKieRule.setPath(agileDroolsModel.getId() + ".drl");
                agileKieTemplate.addContent(agileKieRule);
            }
        } catch (Exception e) {
            logger.error("初始化加载对象发生异常！", e);
        }
    }
}
