package com.jeeagile.frame.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jeeagile.core.security.context.AgileSecurityContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 填充公共字段
 */
public class AgileMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_USER_FIELD_NAME = "createUser";
    private static final String CREATE_TIME_FIELD_NAME = "createTime";

    private static final String UPDATE_USER_FIELD_NAME = "updateUser";
    private static final String UPDATE_TIME_FIELD_NAME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        handlerFieldValByName(CREATE_USER_FIELD_NAME, metaObject);
        handlerFieldValByName(UPDATE_USER_FIELD_NAME, metaObject);

        handlerFieldValByName(CREATE_TIME_FIELD_NAME, metaObject);
        handlerFieldValByName(UPDATE_TIME_FIELD_NAME, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        handlerFieldValByName(UPDATE_USER_FIELD_NAME, metaObject);

        handlerFieldValByName(UPDATE_TIME_FIELD_NAME, metaObject);
    }

    private void handlerFieldValByName(String fieldName, MetaObject metaObject) {
        if (metaObject.hasGetter(fieldName)) {
            Object objectValue = metaObject.getValue(fieldName);
            if (objectValue == null) {
                Object fieldVal = null;
                if (fieldName.equals(CREATE_USER_FIELD_NAME) || fieldName.equals(UPDATE_USER_FIELD_NAME)) {
                    fieldVal = AgileSecurityContext.getUserId();
                } else if (fieldName.equals(CREATE_TIME_FIELD_NAME) || fieldName.equals(UPDATE_TIME_FIELD_NAME)) {
                    fieldVal = new Date();
                }
                setFieldValByName(fieldName, fieldVal, metaObject);
            }
        }
    }
}
