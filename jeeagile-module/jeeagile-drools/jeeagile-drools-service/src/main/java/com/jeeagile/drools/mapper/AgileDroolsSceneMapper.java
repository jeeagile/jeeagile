package com.jeeagile.drools.mapper;

import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.entity.AgileDroolsRule;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.drools.entity.AgileDroolsScene;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 规则场景 Mapper接口
 */
@AgileMapper
public interface AgileDroolsSceneMapper extends AgileBaseMapper<AgileDroolsScene> {
    /**
     * 根据场景ID获取场景所关联的数据对象
     *
     * @param sceneId
     * @return
     */
    @Select(" SELECT M.* FROM" +
            "   agile_drools_scene S," +
            "   agile_drools_scene_rule SR," +
            "   agile_drools_rule_model RM," +
            "   agile_drools_model M " +
            " WHERE " +
            "   S.ID = SR.SCENE_ID " +
            "   AND SR.RULE_ID = RM.RULE_ID " +
            "   AND RM.MODEL_ID = M.ID " +
            "   AND S.ID= #{sceneId} ")
    List<AgileDroolsModel> getDroolsSceneRuleModelList(@Param("sceneId") String sceneId);


    /**
     * 根据场景ID获取场景所关联的数据对象
     *
     * @param sceneId
     * @return
     */
    @Select(" SELECT M.* FROM" +
            "   agile_drools_scene S," +
            "   agile_drools_scene_rule SR," +
            "   agile_drools_rule_model RM," +
            "   agile_drools_model M , " +
            "   agile_drools_model_field F " +
            " WHERE " +
            "   S.ID = SR.SCENE_ID " +
            "   AND SR.RULE_ID = RM.RULE_ID " +
            "   AND RM.MODEL_ID = M.ID " +
            "   AND M.ID = F.OBJECT_ID " +
            "   AND S.ID= #{sceneId} ")
    List<AgileDroolsModel> getDroolsModelFieldObjectList(@Param("sceneId") String sceneId);

    /**
     * 根据场景ID获取场景所关联的数据对象
     *
     * @param sceneId
     * @return
     */
    @Select(" SELECT R.ID,R.RULE_CODE,R.RULE_NAME,R.RULE_PACKAGE FROM" +
            "   agile_drools_scene S," +
            "   agile_drools_scene_rule SR," +
            "   agile_drools_rule R" +
            " WHERE " +
            "   S.ID = SR.SCENE_ID " +
            "   AND SR.RULE_ID = R.ID " +
            "   AND S.ID= #{sceneId} ")
    List<AgileDroolsRule> getDroolsSceneRuleList(@Param("sceneId") String sceneId);
}
