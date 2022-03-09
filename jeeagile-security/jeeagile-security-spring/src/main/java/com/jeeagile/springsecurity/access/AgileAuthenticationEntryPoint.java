package com.jeeagile.springsecurity.access;/**
 * @author JeeAgile
 * @date 2021-07-11
 * @description
 */

import com.alibaba.fastjson.JSON;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AgileAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        AgileResult resultData = AgileResult.agileResult(AgileResultCode.FAIL_USER_INFO);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSON.toJSONString(resultData));
    }
}