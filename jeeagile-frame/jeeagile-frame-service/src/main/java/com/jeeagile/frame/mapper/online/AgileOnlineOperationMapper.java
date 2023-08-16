package com.jeeagile.frame.mapper.online;

import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.vo.online.OnlineColumnData;
import com.jeeagile.frame.vo.online.OnlineFieldFilter;
import com.jeeagile.frame.vo.online.OnlineJoinTable;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-07-31
 * @description 在线表单 表单操作
 */
@AgileMapper
public interface AgileOnlineOperationMapper {
    /**
     * 执行动态查询，查询字典数据
     *
     * @param tableName   表名称
     * @param selectFields 返回字段列表，逗号分隔
     * @param filterList  SQL过滤条件列表
     * @return 查询结果集
     */
    @Select("<script>"
            + "SELECT ${selectFields} FROM ${tableName} "
            + "<where>"
            + "    <if test=\"filterList != null\">"
            + "        <foreach collection=\"filterList\" item=\"filter\">"
            + "            <if test=\"filter.filterType == '02'\">"
            + "                AND ${filter.columnName} = #{filter.columnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '05'\">"
            + "                AND ${filter.columnName} IN "
            + "                <foreach collection=\"filter.columnValueList\" item=\"columnValue\" separator=\",\" open=\"(\" close=\")\">"
            + "                    #{columnValue} "
            + "                </foreach>"
            + "            </if>"
            + "        </foreach>"
            + "    </if>"
            + "</where>"
            + "</script>")
    List<Map> getDictDataList(@Param("tableName") String tableName,
                              @Param("selectFields") String selectFields,
                              @Param("filterList") List<OnlineFieldFilter> filterList);

    /**
     * 执行动态SQL，分页查询表数据
     *
     * @param tableName    表名称
     * @param joinInfoList 关联表信息列表
     * @param selectFields  返回字段列表，逗号分隔
     * @param filterList   SQL过滤条件列表
     * @param orderBy      排序字符串
     * @return 查询结果集
     */
    @Select("<script>"
            + "SELECT ${selectFields} FROM ${tableName} "
            + "<if test=\"joinInfoList != null\">"
            + "    <foreach collection=\"joinInfoList\" item=\"joinInfo\">"
            + "        <if test=\"joinInfo.leftJoin\">"
            + "            LEFT JOIN ${joinInfo.joinTableName} ON ${joinInfo.joinCondition}"
            + "        </if>"
            + "        <if test=\"!joinInfo.leftJoin\">"
            + "            INNER JOIN ${joinInfo.joinTableName} ON ${joinInfo.joinCondition}"
            + "        </if>"
            + "    </foreach>"
            + "</if>"
            + "<where>"
            + "    <if test=\"filterList != null\">"
            + "        <foreach collection=\"filterList\" item=\"filter\">"
            + "            <if test=\"filter.filterType == '02'\">"
            + "                AND ${filter.tableName}.${filter.columnName} = #{filter.columnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '03'\">"
            + "                AND ${filter.tableName}.${filter.columnName} &gt;= #{filter.columnValueStart} "
            + "                AND ${filter.tableName}.${filter.columnName} &lt;= #{filter.columnValueEnd} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '04'\">"
            + "                <bind name = \"safeColumnValue\" value = \"'%' + filter.columnValue + '%'\" />"
            + "                AND ${filter.tableName}.${filter.columnName} LIKE #{safeColumnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '05'\">"
            + "                AND ${filter.tableName}.${filter.columnName} IN "
            + "                <foreach collection=\"filter.columnValueList\" item=\"columnValue\" separator=\",\" open=\"(\" close=\")\">"
            + "                    #{columnValue} "
            + "                </foreach>"
            + "            </if>"
            + "        </foreach>"
            + "    </if>"
            + "</where>"
            + "<if test=\"orderBy != null and orderBy != ''\">"
            + "    ORDER BY ${orderBy}"
            + "</if>"
            + "</script>")
    AgilePage<Map> getPageData(AgilePage<Map> agilePage,
                               @Param("tableName") String tableName,
                               @Param("selectFields") String selectFields,
                               @Param("joinInfoList") List<OnlineJoinTable> joinInfoList,
                               @Param("filterList") List<OnlineFieldFilter> filterList,
                               @Param("orderBy") String orderBy);


    /**
     * 执行动态SQL，查询表数据
     *
     * @param tableName    主表名称
     * @param joinInfoList 关联表信息列表
     * @param selectFields  返回字段列表，逗号分隔
     * @param filterList   SQL过滤条件列表
     * @return 查询结果
     */
    @Select("<script>"
            + "SELECT ${selectFields} FROM ${tableName} "
            + "<if test=\"joinInfoList != null\">"
            + "    <foreach collection=\"joinInfoList\" item=\"joinInfo\">"
            + "        <if test=\"joinInfo.leftJoin\">"
            + "            LEFT JOIN ${joinInfo.joinTableName} ON ${joinInfo.joinCondition}"
            + "        </if>"
            + "        <if test=\"!joinInfo.leftJoin\">"
            + "            INNER JOIN ${joinInfo.joinTableName} ON ${joinInfo.joinCondition}"
            + "        </if>"
            + "    </foreach>"
            + "</if>"
            + "<where>"
            + "    <if test=\"filterList != null\">"
            + "        <foreach collection=\"filterList\" item=\"filter\">"
            + "            <if test=\"filter.filterType == '02'\">"
            + "                AND ${filter.tableName}.${filter.columnName} = #{filter.columnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '03'\">"
            + "                AND ${filter.tableName}.${filter.columnName} &gt;= #{filter.columnValueStart} "
            + "                AND ${filter.tableName}.${filter.columnName} &lt;= #{filter.columnValueEnd} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '04'\">"
            + "                <bind name = \"safeColumnValue\" value = \"'%' + filter.columnValue + '%'\" />"
            + "                AND ${filter.tableName}.${filter.columnName} LIKE #{safeColumnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '05'\">"
            + "                AND ${filter.tableName}.${filter.columnName} IN "
            + "                <foreach collection=\"filter.columnValueList\" item=\"columnValue\" separator=\",\" open=\"(\" close=\")\">"
            + "                    #{columnValue} "
            + "                </foreach>"
            + "            </if>"
            + "        </foreach>"
            + "    </if>"
            + "</where>"
            + "</script>")
    Map getOneData(@Param("tableName") String tableName,
                   @Param("selectFields") String selectFields,
                   @Param("joinInfoList") List<OnlineJoinTable> joinInfoList,
                   @Param("filterList") List<OnlineFieldFilter> filterList);

    /**
     * 执行动态SQL,插入表数据
     *
     * @param tableName       表名称
     * @param columnNames      字段名，逗号隔开
     * @param columnValueList 字段值
     */
    @Insert("<script>"
            + "INSERT INTO ${tableName} (${columnNames}) VALUES "
            + "    <foreach collection=\"columnValueList\" item=\"columnValue\" separator=\",\" open=\"(\" close=\")\">"
            + "        #{columnValue} "
            + "    </foreach>"
            + "</script>")
    void insert(@Param("tableName") String tableName,
                @Param("columnNames") String columnNames,
                @Param("columnValueList") List<Object> columnValueList);

    /**
     * 执行动态SQL 更新表数据
     *
     * @param tableName        数据表名。
     * @param updateColumnList 更新字段列表。
     * @param whereColumnList  过滤字段列表。
     * @return 更新行数。
     */
    @Update("<script>"
            + "UPDATE ${tableName} SET "
            + "    <foreach collection=\"updateColumnList\" item=\"columnData\" separator=\",\" >"
            + "        <if test=\"columnData.columnValue != null\">"
            + "            ${columnData.columnName} = #{columnData.columnValue} "
            + "        </if>"
            + "        <if test=\"columnData.columnValue == null\">"
            + "            ${columnData.columnName} = NULL "
            + "        </if>"
            + "    </foreach>"
            + "<where>"
            + "    <foreach collection=\"whereColumnList\" item=\"columnData\" >"
            + "        AND ${columnData.columnName} = #{columnData.columnValue}"
            + "    </foreach>"
            + "</where>"
            + "</script>")
    int update(@Param("tableName") String tableName,
               @Param("updateColumnList") List<OnlineColumnData> updateColumnList,
               @Param("whereColumnList") List<OnlineColumnData> whereColumnList);


    /**
     * 执行动态SQL 删除指定数据
     *
     * @param tableName  表名
     * @param filterList SQL过滤条件列表
     * @return 删除行数
     */
    @Delete("<script>"
            + "DELETE FROM ${tableName} "
            + "<where>"
            + "    <if test=\"filterList != null\">"
            + "        <foreach collection=\"filterList\" item=\"filter\">"
            + "            <if test=\"filter.filterType == '02'\">"
            + "                AND ${filter.tableName}.${filter.columnName} = #{filter.columnValue} "
            + "            </if>"
            + "            <if test=\"filter.filterType == '05'\">"
            + "                AND ${filter.tableName}.${filter.columnName} IN "
            + "                <foreach collection=\"filter.columnValueList\" item=\"columnValue\" separator=\",\" open=\"(\" close=\")\">"
            + "                    #{columnValue} "
            + "                </foreach>"
            + "            </if>"
            + "        </foreach>"
            + "    </if>"
            + "</where>"
            + "</script>")
    int delete(@Param("tableName") String tableName,
               @Param("filterList") List<OnlineFieldFilter> filterList);
}
