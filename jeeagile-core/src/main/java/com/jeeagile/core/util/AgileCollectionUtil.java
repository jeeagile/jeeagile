package com.jeeagile.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-03-10
 * @description 集合工具类
 */
public class AgileCollectionUtil {
    /**
     * 校验集合是否为空
     *
     * @param collection 入参
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 校验集合是否不为空
     *
     * @param collection 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

}
