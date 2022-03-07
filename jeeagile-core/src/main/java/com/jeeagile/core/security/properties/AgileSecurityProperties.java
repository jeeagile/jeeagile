package com.jeeagile.core.security.properties;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.util.AgileArrayUtil;
import com.jeeagile.core.util.AgileStringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private static String[] FILTER_ANON_URL = {"**" , "/**"};
    /**
     * 无需认证URL
     */
    private List<String> anonUrl;

    /**
     * session超时时间
     */
    private long sessionTimeOut = 180000L;

    public List<String> getAnonUrl() {
        List<String> filterUrl = new ArrayList<>();
        if (!CollectionUtils.isEmpty(anonUrl)) {
            for (String url : anonUrl) {
                if (AgileStringUtil.isNotEmpty(url) && !AgileArrayUtil.contains(FILTER_ANON_URL, url)) {
                    filterUrl.add(url);
                }
            }
        }
        return filterUrl;
    }
}
