package com.jeeagile.frame.plugins;

import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDataScope;
import com.jeeagile.frame.plugins.datascope.property.AgileDataScopeProperty;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class AgileDataScopeHelper {
    private static Map<String, AgileDataScopeProperty> agileDataScopeMap;
    private static Set<String> agileDataScopeSet;

    static {
        agileDataScopeMap = new ConcurrentHashMap<>();
        agileDataScopeSet = new ConcurrentSkipListSet<>();
    }

    /**
     * 获取 Mapper方法自定义权限注解
     *
     * @param mapperStatementId
     * @return
     */
    public static AgileDataScopeProperty getAgileDataScope(String mapperStatementId) {
        AgileDataScopeProperty agileDataScope = agileDataScopeMap.get(mapperStatementId);
        if (agileDataScope == null) {
            String mapperClassPath = getMapperClassPath(mapperStatementId);
            if (AgileStringUtil.isEmpty(mapperClassPath)) {
                return null;
            }
            //如果存在说明已进行过扫描
            if (agileDataScopeSet.contains(mapperClassPath)) {
                return null;
            }
            try {
                Class mapperClass = Class.forName(mapperClassPath);
                for (Method method : mapperClass.getMethods()) {
                    AgileDataScope methodAgileDataScope = method.getAnnotation(AgileDataScope.class);
                    if (methodAgileDataScope != null) {
                        agileDataScopeMap.put(mapperClassPath + "." + method.getName(), new AgileDataScopeProperty(methodAgileDataScope));
                    }
                }
                AgileDataScope classAgileDataScope = (AgileDataScope) mapperClass.getAnnotation(AgileDataScope.class);
                if (classAgileDataScope != null) {
                    agileDataScopeMap.put(mapperClassPath, new AgileDataScopeProperty(classAgileDataScope));
                } else {
                    agileDataScopeSet.add(mapperClassPath);
                }
                agileDataScope = agileDataScopeMap.get(mapperStatementId);
                if (agileDataScope == null) {
                    agileDataScope = agileDataScopeMap.get(mapperClassPath);
                }
            } catch (ClassNotFoundException e) {
                log.warn("数据权限提取异常《{}》,请进行核实！", mapperStatementId);
            }
        }
        return agileDataScope;
    }

    /**
     * 截取 Mapper 类路径
     *
     * @param mappedStatementId
     * @return
     */
    private static String getMapperClassPath(String mappedStatementId) {
        int indexOf = mappedStatementId.lastIndexOf(".");
        return indexOf > 0 ? mappedStatementId.substring(0, indexOf) : null;
    }
}
