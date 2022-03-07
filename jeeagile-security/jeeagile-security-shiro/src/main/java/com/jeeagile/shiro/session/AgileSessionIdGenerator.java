package com.jeeagile.shiro.session;

import com.jeeagile.core.util.AgileStringUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileSessionIdGenerator implements SessionIdGenerator {
    public AgileSessionIdGenerator() {
    }

    public Serializable generateId(Session session) {
        return AgileStringUtil.getUuid();
    }
}