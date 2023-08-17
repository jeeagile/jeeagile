package com.jeeagile.online.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.online.vo.OnlineQueryParam;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-14
 * @description 在线表单 表单操作  接口
 */
public interface IAgileOnlineOperationService {
    /**
     * 查询字典数据
     *
     * @param onlineQueryParam
     * @return
     */
    List<Map> selectDictData(OnlineQueryParam onlineQueryParam);

    /**
     * 分页查询数据
     *
     * @param agilePageable
     * @return
     */
    AgilePage<Map> selectPageData(AgilePageable<OnlineQueryParam> agilePageable);

    /**
     * 查看数据明细
     *
     * @param tableId 数据表Id
     * @param dataId  数据ID
     * @return
     */
    Map selectOneData(String tableId, String dataId);

    /**
     * 保存数据
     *
     * @param tableId    数据表ID
     * @param masterData 主表数据
     * @param slaveData  从表数据
     * @return
     */
    boolean saveTableData(String tableId, Map masterData, Map slaveData);

    /**
     * 更新数据
     *
     * @param tableId    数据表ID
     * @param masterData 表数据
     * @return
     */
    boolean updateTableData(String tableId, Map masterData);

    /**
     * 删除数据
     *
     * @param tableId 数据表ID
     * @param dataId  数据ID
     * @return
     */
    boolean deleteTableData(String tableId, String dataId);
}
