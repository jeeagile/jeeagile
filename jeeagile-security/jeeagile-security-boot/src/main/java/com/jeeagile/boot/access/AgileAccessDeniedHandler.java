package com.jeeagile.boot.access;

import com.alibaba.fastjson.JSON;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AgileAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        AgileResult resultData = AgileResult.agileResult(AgileResultCode.FAIL_USER_PERMS);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSON.toJSONString(resultData));
    }
}
