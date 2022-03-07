package com.jeeagile.core.cache.code;

import com.jeeagile.core.cache.code.annotation.AgileOptionCode;
import com.jeeagile.core.cache.code.annotation.AgileOptionName;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.AgileArrayUtil;
import com.jeeagile.core.util.StringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 代码缓存配置类
 */
@Data
@Slf4j
public class AgileCodeCacheConfig {

    private Object bean;
    private Method method;
    private String cacheName;
    private String cacheDesc;
    private Set<String> optionCodeField = new LinkedHashSet<>();
    private Set<String> optionNameField = new LinkedHashSet<>();
    private Set<Field> objectOptionCodeField = new LinkedHashSet<>();
    private Set<Field> objectOptionNameField = new LinkedHashSet<>();

    @Getter
    private int dataSize = 0;

    private static final String AGILE_CODE_CACHE_NAME = "code";

    private String getCodeCacheListKey() {
        return cacheName.toLowerCase() + "_list";
    }

    private String getCodeCacheMapKey() {
        return cacheName.toLowerCase() + "_map";
    }

    /**
     * 获取代码值
     *
     * @param object
     * @return
     */
    private String getOptionCodeByObject(Object object) {
        String optionCodeValue;
        if (object instanceof AgileBaseCodeCache) {
            optionCodeValue = ((AgileBaseCodeCache) object).getOptionCode();
        } else if (object instanceof Map) {
            optionCodeValue = getOptionValueByMap(object, this.optionCodeField);
        } else {
            this.loadObjectOptionField(object);
            optionCodeValue = getOptionValueByObject(object, this.objectOptionCodeField);
        }
        return optionCodeValue;
    }

    /**
     * 获取代码名称
     *
     * @param object
     * @return
     */
    private String getOptionNameByObject(Object object) {
        String optionNameValue = null;
        if (object instanceof AgileBaseCodeCache) {
            optionNameValue = ((AgileBaseCodeCache) object).getOptionName();
        } else if (object instanceof Map) {
            optionNameValue = getOptionValueByMap(object, this.optionNameField);
        } else {
            this.loadObjectOptionField(object);
            optionNameValue = getOptionValueByObject(object, this.objectOptionNameField);
        }
        return optionNameValue;
    }

    /**
     * 通过map对象获取option
     *
     * @param object
     * @param optionField
     * @return
     */
    private String getOptionValueByMap(Object object, Set<String> optionField) {
        StringBuilder optionValue = new StringBuilder("");
        if (optionField != null && !optionField.isEmpty()) {
            for (String fieldName : optionField) {
                optionValue.append((String) ((Map) object).get(fieldName));
            }
        }
        return optionValue.toString();
    }

    /**
     * 通过object对象获取option
     *
     * @param object
     * @param optionField
     * @return
     */
    private String getOptionValueByObject(Object object, Set<Field> optionField) {
        StringBuilder optionValue = new StringBuilder("");
        if (optionField != null && !optionField.isEmpty()) {
            try {
                for (Field field : optionField) {
                    optionValue.append(field.get(object).toString());
                }
            } catch (Exception ex) {
                log.error("获取《》代码名称异常！", this.getCacheDesc());
            }
        }
        return optionValue.toString();
    }

    /**
     * 执行代码表缓存方法
     *
     * @return
     */
    private List<Object> invokeAgileCodeDataMethod() {
        try {
            return (List) method.invoke(bean);
        } catch (Exception ex) {
            throw new AgileFrameException("执行代码表缓存方法异常！", ex);
        }
    }

    /**
     * 放缓存
     *
     * @param codeDataList
     * @throws Exception
     */
    protected void putAgileCodeCache(List<Object> codeDataList) {
        if (!CollectionUtils.isEmpty(codeDataList)) {
            this.dataSize = codeDataList.size();
            Map<String, Object> codeDataMap = this.getCodeDataMap(codeDataList);
            AgileCacheUtil.put(AGILE_CODE_CACHE_NAME, getCodeCacheListKey(), codeDataList);
            AgileCacheUtil.put(AGILE_CODE_CACHE_NAME, getCodeCacheMapKey(), codeDataMap);
            log.info("已成功初始化代码缓存《{}:{}》：共{}条数据!", this.cacheName, this.cacheDesc, this.dataSize);
        }
    }

    /**
     * 组装Map缓存
     *
     * @param codeDataList
     */
    private Map<String, Object> getCodeDataMap(List<Object> codeDataList) {
        Map<String, Object> codeDataMap = new HashMap<>();
        for (Object object : codeDataList) {
            String optionCodeValue = getOptionCodeByObject(object);
            String optionNameValue = getOptionNameByObject(object);
            if (StringUtil.isEmpty(optionCodeValue) || StringUtil.isEmpty(optionNameValue)) {
                log.error("OptionCode和OptionName字段值获取为空，请进行检查！");
                throw new AgileFrameException("请检查OptionCode和OptionName字段值配置是否正确！");
            }
            codeDataMap.put(optionCodeValue, object);
        }
        return codeDataMap;
    }

    /**
     * 加载ObjectOptionField
     */
    @SuppressWarnings("all")
    private void loadObjectOptionField(Object object) {
        if (this.objectOptionCodeField.isEmpty() || this.objectOptionNameField.isEmpty()) {
            for (Field field : object.getClass().getFields()) {
                String fieldName = field.getName();
                if (AgileArrayUtil.contains(this.optionCodeField.toArray(), fieldName)) {
                    this.objectOptionCodeField.add(field);
                }
                if (AgileArrayUtil.contains(this.optionCodeField.toArray(), fieldName)) {
                    this.objectOptionNameField.add(field);
                }
                Annotation codeAnnotation = field.getAnnotation(AgileOptionCode.class);
                if (codeAnnotation != null) {
                    this.objectOptionCodeField.add(field);
                }
                Annotation nameAnnotation = field.getAnnotation(AgileOptionName.class);
                if (nameAnnotation != null) {
                    this.objectOptionNameField.add(field);
                }
            }
        }
    }

    /**
     * 将代码数据放入缓存
     *
     * @throws Exception
     */
    protected void putAgileCodeCache() {
        this.putAgileCodeCache(invokeAgileCodeDataMethod());
    }

    /**
     * 更新缓存
     *
     * @throws Exception
     */
    protected synchronized List<Object> updateAgileCodeCache() {
        List<Object> codeDataList = (List) AgileCacheUtil.get(AGILE_CODE_CACHE_NAME, getCodeCacheListKey());
        if (codeDataList != null) {
            codeDataList = this.invokeAgileCodeDataMethod();
            this.putAgileCodeCache(codeDataList);
        }
        return codeDataList;
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    protected List<Object> getAgileCodeDataList() {
        try {
            List<Object> codeDataList = (List) AgileCacheUtil.get(AGILE_CODE_CACHE_NAME, getCodeCacheListKey());
            if (codeDataList == null) {
                codeDataList = this.updateAgileCodeCache();
            }
            return codeDataList;
        } catch (Exception ex) {
            log.warn("获取代码表数据《{}》异常！", getCacheDesc());
            return new ArrayList<>();
        }
    }

    /**
     * 获取map结构数据
     *
     * @return
     */
    private Map<String, Object> getCodeDataMap() {
        try {
            Map<String, Object> codeDataMap = (Map) AgileCacheUtil.get(AGILE_CODE_CACHE_NAME, getCodeCacheMapKey());
            if (codeDataMap == null) {
                List<Object> codeDataList = this.updateAgileCodeCache();
                codeDataMap = this.getCodeDataMap(codeDataList);
            }
            return codeDataMap;
        } catch (Exception ex) {
            log.warn("获取代码表数据《{}》异常！", getCacheDesc());
            return null;
        }
    }

    /**
     * 根据编码获取代码缓存对象
     *
     * @param optionCode
     * @return
     */
    protected Object getObjectByCode(String optionCode) {
        Map<String, Object> codeDataMap = getCodeDataMap();
        if (!CollectionUtils.isEmpty(codeDataMap)) {
            return codeDataMap.get(optionCode);
        } else {
            return null;
        }
    }

    /**
     * 根据代码值获取代码名称
     *
     * @param optionCode
     * @return
     */
    protected String getNameByCode(String optionCode) {
        Object codeDataObject = getObjectByCode(optionCode);
        if (codeDataObject != null) {
            return getOptionNameByObject(codeDataObject);
        } else {
            return null;
        }
    }
}
