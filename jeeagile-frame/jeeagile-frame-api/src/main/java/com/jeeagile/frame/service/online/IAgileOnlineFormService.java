package com.jeeagile.frame.service.online;

import com.jeeagile.frame.entity.online.AgileOnlineForm;
import com.jeeagile.frame.service.IAgileBaseService;

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
    boolean changeFormStatus(String id, String formStatus);
}
