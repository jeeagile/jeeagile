package com.jeeagile.frame.service.online;

import com.jeeagile.frame.entity.online.AgileOnlineColumn;
import com.jeeagile.frame.entity.online.AgileOnlineTable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.frame.vo.online.OnlinePageTable;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-07-25
 * @description 在线表单 表单数据模型 接口
 */
public interface IAgileOnlineTableService extends IAgileBaseService<AgileOnlineTable> {
    /**
     * 新增表字段
     *
     * @param agileOnlineColumn
     * @return
     */
    AgileOnlineColumn addOnlineColumn(AgileOnlineColumn agileOnlineColumn);

    /**
     * 新增表字段
     *
     * @param agileOnlineColumn
     * @return
     */
    AgileOnlineColumn refreshOnlineColumn(AgileOnlineColumn agileOnlineColumn);

    /**
     * 根据表单页面ID获取表单页面对应的数据表
     *
     * @param pageId
     * @return
     */
    List<OnlinePageTable> pageTableList(String pageId);
}
