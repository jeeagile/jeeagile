package com.jeeagile.shiro.session;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.util.AgileStringUtil;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-07-03
 * @description
 */
public class AgileWebSessionManager extends DefaultWebSessionManager {
    public AgileWebSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        Serializable sessionId = super.getSessionId(request, response);
        if (sessionId != null && AgileStringUtil.isNotEmpty(sessionId.toString())) {
            return sessionId;
        } else {
            String userToken = WebUtils.toHttp(request).getHeader(AgileConstants.AGILE_TOKEN);
            if (AgileStringUtil.isEmpty(userToken)) {
                userToken = request.getParameter(AgileConstants.AGILE_TOKEN);
            }
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, userToken);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return userToken;
        }
    }
}
