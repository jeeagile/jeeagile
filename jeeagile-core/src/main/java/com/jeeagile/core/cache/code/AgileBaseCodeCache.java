package com.jeeagile.core.cache.code;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 代码缓存基类
 */
public interface AgileBaseCodeCache {

    /**
     * 获取单元对象值代码
     *
     * @return
     */
    String getOptionCode();

    /**
     * 获取单元对象名称
     *
     * @return
     */
    String getOptionName();
}
