package com.jeeagile.drools.mapper;

import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.drools.entity.AgileDroolsSceneLogger;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景执行日志 Mapper接口
 */
@AgileMapper
public interface AgileDroolsSceneLoggerMapper extends AgileBaseMapper<AgileDroolsSceneLogger> {
    /**
     * 总执行次数
     * @param sceneId
     * @return
     */
    @Select(" SELECT count(1)" +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   SCENE_ID = #{sceneId} ")
    Integer getExecuteCount(@Param("sceneId") String sceneId);

    /**
     * 执行成功次数
     * @param sceneId
     * @return
     */
    @Select(" SELECT count(1)" +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   EXECUTE_STATUS = '1' " +
            "   AND SCENE_ID = #{sceneId} ")
    Integer getSuccessCount(@Param("sceneId") String sceneId);
    /**
     * 执行失败次数
     * @param sceneId
     * @return
     */
    @Select(" SELECT count(1)" +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   EXECUTE_STATUS = '0' " +
            "   AND SCENE_ID = #{sceneId} ")
    Integer getErrorCount(@Param("sceneId") String sceneId);

    /**
     * 最长执行时间
     * @param sceneId
     * @return
     */
    @Select(" SELECT MAX(EXECUTE_TIME) " +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   SCENE_ID = #{sceneId} ")
    Integer getMaxTime(@Param("sceneId") String sceneId);

    /**
     * 最短执行时间
     * @param sceneId
     * @return
     */
    @Select(" SELECT MIN(EXECUTE_TIME) " +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   SCENE_ID = #{sceneId} ")
    Integer getMinTime(@Param("sceneId") String sceneId);

    /**
     * 平均执行时间
     * @param sceneId
     * @return
     */
    @Select(" SELECT AVG(EXECUTE_TIME) " +
            " FROM " +
            "   AGILE_DROOLS_SCENE_LOGGER " +
            " WHERE " +
            "   SCENE_ID = #{sceneId} ")
    Integer getAverageTime(@Param("sceneId") String sceneId);

    /**
     * 按日统计总执行次数
     * @param agileDroolsSceneLogger
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(START_TIME,'%Y-%m-%d') executeDate, COUNT(1) executeCount" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d') " +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticExecuteCount(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);

    /**
     * 按日统计成功执行次数
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(START_TIME,'%Y-%m-%d') executeDate, COUNT(1) successCount" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       EXECUTE_STATUS = '1' " +
            "       AND START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d') " +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticSuccessCount(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);

    /**
     * 按日统计失败执行次数
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(START_TIME,'%Y-%m-%d') executeDate, COUNT(1) errorCount" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       EXECUTE_STATUS = '0' " +
            "       AND START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d') " +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticErrorCount(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);
    /**
     * 按日统计最长执行时间
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(start_time,'%Y-%m-%d') executeDate, MAX(execute_time) maxTime" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(start_time,'%Y-%m-%d')" +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticMaxTime(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);

    /**
     * 按日统计平均执行时间
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(START_TIME,'%Y-%m-%d') executeDate, AVG(EXECUTE_TIME) averageTime" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d')" +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticAverageTime(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);

    /**
     * 按日统计最短执行时间
     * @return
     */
    @Select(" SELECT * FROM (" +
            "   SELECT DATE_FORMAT(START_TIME,'%Y-%m-%d') executeDate, MIN(EXECUTE_TIME) minTime" +
            "   FROM " +
            "       AGILE_DROOLS_SCENE_LOGGER " +
            "   WHERE " +
            "       START_TIME >= #{agileDroolsSceneLogger.startTime} " +
            "       AND START_TIME <= #{agileDroolsSceneLogger.endTime} " +
            "       AND SCENE_ID = #{agileDroolsSceneLogger.sceneId} " +
            "   GROUP BY DATE_FORMAT(START_TIME,'%Y-%m-%d')" +
            "  ) t ORDER BY t.executeDate ")
    List<Map> statisticMinTime(@Param("agileDroolsSceneLogger") AgileDroolsSceneLogger agileDroolsSceneLogger);
}
