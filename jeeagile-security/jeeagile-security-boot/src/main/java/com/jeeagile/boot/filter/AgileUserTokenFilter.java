package com.jeeagile.boot.filter;

import com.jeeagile.boot.userdetails.AgileUserDetails;
import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AgileUserTokenFilter extends OncePerRequestFilter {
    @Lazy
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String userToken = AgileServletUtil.getUserToken(httpServletRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (AgileStringUtil.isNotEmpty(userToken) && securityContext.getAuthentication() == null) {
            AgileUserDetails agileUserDetails = (AgileUserDetails) AgileCacheUtil.get(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, userToken);
            if (agileUserDetails != null && AgileStringUtil.isNotEmpty(agileUserDetails.getUsername())) {
                AgileCacheUtil.put(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, userToken, agileUserDetails);
                SessionInformation sessionInformation = sessionRegistry.getSessionInformation(userToken);
                if (sessionInformation == null) {
                    sessionRegistry.registerNewSession(userToken, userToken);
                } else {
                    sessionRegistry.refreshLastRequest(userToken);
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(agileUserDetails, null, agileUserDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                sessionRegistry.removeSessionInformation(userToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
