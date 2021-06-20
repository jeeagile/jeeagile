package com.jeeagile.core.security.properties;

import com.jeeagile.core.constants.AgileConstants;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(AgileConstants.AGILE_SECURITY)
public class AgileSecurityProperties {
    /**
     * 无需认证URL
     */
    private List<String> anonUrl;

    /**
     * session超时时间
     */
    private long sessionTimeOut = 180000L;
}
