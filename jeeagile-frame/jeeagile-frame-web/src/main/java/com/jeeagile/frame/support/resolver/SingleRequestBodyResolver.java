package com.jeeagile.frame.support.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public class SingleRequestBodyResolver implements HandlerMethodArgumentResolver {
    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(SingleRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        SingleRequestBody singleRequestBody = methodParameter.getParameterAnnotation(SingleRequestBody.class);
        Assert.state(singleRequestBody != null, "Unresolvable singleRequestBody");
        final String httpRequestBody = getHttpRequestBody(nativeWebRequest);
        Assert.state(httpRequestBody != null, "Unresolvable httpRequestBody");
        JSONObject jsonObject = getJsonObjectByHttpRequestBody(httpRequestBody);
        String parameterName = AgileStringUtil.isEmpty(singleRequestBody.name()) ? methodParameter.getParameterName() : singleRequestBody.name();
        Assert.state(parameterName != null, "Unresolvable parameter name");
        Class<?> parameterType = methodParameter.getParameterType();
        Object object = null;
        if (jsonObject == null) {
            object = getValueObjectByHttpRequestBody(parameterType, httpRequestBody);
        } else {
            object = getValueObjectByJsonObject(methodParameter, parameterName, jsonObject);
        }
        if (singleRequestBody.required()) {
            if (object == null) {
                throw new AgileFrameException(String.format("required param %s is not present", parameterName));
            }
        } else {
            if (object == null && !singleRequestBody.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
                if (isBasicDataTypes(parameterType)) {// ?????????????????????
                    object = parseBasicTypeWrapper(parameterType, singleRequestBody.defaultValue());
                } else if (parameterType == String.class) { // ???????????????
                    object = singleRequestBody.defaultValue();
                }
            }
        }
        return object;
    }

    /**
     * ????????????body?????????
     *
     * @param parameterType
     * @param requestBody
     * @return
     */
    private Object getValueObjectByHttpRequestBody(Class<?> parameterType, String requestBody) {
        Object object = null;
        if (isBasicDataTypes(parameterType)) {// ?????????????????????
            object = parseBasicTypeWrapper(parameterType, requestBody);
        } else if (parameterType == String.class) { // ???????????????
            object = requestBody;
        } else if (parameterType == Serializable.class) { // ???????????????
            object = requestBody;
        } else {
            throw new AgileFrameException("SingleRequestBody???????????????????????????JSON??????????????????" + requestBody + "???Can not cast to JSONObject???");
        }
        return object;
    }

    /**
     * ??????jsonObject?????????
     *
     * @param methodParameter
     * @param parameterName
     * @param jsonObject
     * @return
     */
    private Object getValueObjectByJsonObject(MethodParameter methodParameter, String parameterName, JSONObject jsonObject) {
        Object object = null;
        try {
            if (parameterName.contains(".")) {
                String[] parameterNameStr = parameterName.split("\\.");
                parameterName = parameterNameStr[parameterNameStr.length - 1];
                for (int i = 0; i < parameterNameStr.length - 1; i++) {
                    jsonObject = jsonObject.getJSONObject(parameterNameStr[i]);
                    if (jsonObject == null) {
                        break;
                    }
                }
            }
            if (jsonObject != null) {
                object = jsonObject.getObject(parameterName, methodParameter.getParameterType());
                if (methodParameter.getParameterType() == List.class) {
                    ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getGenericParameterType();
                    Class<?> itemClass = (Class) parameterizedType.getActualTypeArguments()[0];
                    if (!itemClass.isPrimitive()) {
                        object = JSON.parseArray(jsonObject.getString(parameterName), itemClass);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("??????SingleValue??????", e);
        }
        return object;
    }

    /**
     * ??????jsonObject??????
     *
     * @param httpRequestBody
     * @return
     */
    private JSONObject getJsonObjectByHttpRequestBody(String httpRequestBody) {
        JSONObject jsonObject = null;
        try {
            if (AgileStringUtil.isNotEmpty(httpRequestBody) && httpRequestBody.contains("{") && httpRequestBody.contains("}")) {
                jsonObject = JSON.parseObject(httpRequestBody);
            }
        } catch (JSONException e) {
            log.warn(e.getMessage());
        }
        return jsonObject;
    }

    /**
     * ??????????????????????????????????????????
     */
    private boolean isBasicDataTypes(Class<?> clazz) {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * ???????????????????????????
     */
    private Object parseBasicTypeWrapper(Class<?> parameterType, String value) {
        try {
            if (Number.class.isAssignableFrom(parameterType)) {
                if (parameterType == Integer.class) {
                    return Integer.parseInt(value);
                } else if (parameterType == Short.class) {
                    return Short.parseShort(value);
                } else if (parameterType == Long.class) {
                    return Long.parseLong(value);
                } else if (parameterType == Float.class) {
                    return Float.parseFloat(value);
                } else if (parameterType == Double.class) {
                    return Double.parseDouble(value);
                } else if (parameterType == Byte.class) {
                    return value.getBytes();
                }
            } else if (parameterType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (parameterType == Character.class) {
                return value.charAt(0);
            }
        } catch (Exception ex) {
            log.error("??????????????????????????????", ex);
            throw new AgileFrameException("????????????????????????????????????????????????" + ex.getMessage());
        }
        return null;
    }

    /**
     * ???????????????JSON?????????
     */

    private String getHttpRequestBody(NativeWebRequest webRequest) {

        // ??????????????????
        String jsonBody = (String) webRequest.getAttribute(JSON_REQUEST_BODY, RequestAttributes.SCOPE_REQUEST);
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        // ???????????????????????????
        if (jsonBody == null && servletRequest != null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(JSON_REQUEST_BODY, jsonBody, RequestAttributes.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new AgileFrameException(e);
            }
        }
        return jsonBody;
    }
}