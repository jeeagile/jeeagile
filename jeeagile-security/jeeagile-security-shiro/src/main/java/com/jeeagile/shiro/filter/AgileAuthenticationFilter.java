package com.jeeagile.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@SuppressWarnings("all")
public class AgileAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (this.isLoginRequest(request, response)) {
            return true;
        } else if (requestURI.equals("/") || requestURI.equals("/index.html")) {
            return true;
        } else {
            AgileResult<?> agileResult = new AgileResult<>(AgileResultCode.FAIL_USER_INFO);
            //设置响应头
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            //写回给客户端
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(agileResult));
            //刷新和关闭输出流
            printWriter.flush();
            printWriter.close();
            return false;
        }
    }
}
