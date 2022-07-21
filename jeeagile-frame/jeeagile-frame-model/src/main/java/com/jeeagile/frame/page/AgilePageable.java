package com.jeeagile.frame.page;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 接收分页查询数据
 */
@Data
public class AgilePageable<T> implements Serializable {
    /**
     * 当前页数
     */
    private int currentPage = 0;

    /**
     * 每页记录数
     */
    private int pageSize = 20;

    /**
     * 查询条件
     */
    @SuppressWarnings("all")
    private T queryCond;

    /**
     * 设置每页记录数默认值
     *
     * @return
     */
    public int getPageSize() {
        if (pageSize < 1) {
            return 20;
        } else {
            return pageSize;
        }
    }

}
