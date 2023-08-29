package com.jeeagile.online.service;

import com.jeeagile.online.entity.AgileOnlineForm;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.online.vo.OnlinePageRender;

import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-07-19
 * @description 在线表单 表单信息 接口
 */
public interface IAgileOnlineFormService extends IAgileBaseService<AgileOnlineForm> {
    /**
     * 表单发布
     *
     * @param id
     * @param publishStatus
     * @return
     */
    boolean publish(String id, String publishStatus);

    /**
     * 修改表单状态
     *
     * @param id
     * @param formStatus
     * @return
     */
    boolean changeStatus(String id, String formStatus);

    /**
     * 加载表单页面
     *
     * @param pageId 表单页面ID
     * @return
     */
    OnlinePageRender render(String pageId);

    /**
     * 获取在线表单和表单页面列表
     *
     * @return
     */
    Map selectFormPageList();
}
