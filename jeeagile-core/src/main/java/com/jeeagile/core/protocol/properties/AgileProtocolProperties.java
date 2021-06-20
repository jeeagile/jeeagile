package com.jeeagile.core.protocol.properties;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.protocol.enums.AgileProtocolType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(AgileConstants.AGILE_PROTOCOL)
public class AgileProtocolProperties {
    private AgileProtocolType type = AgileProtocolType.LOCAL;

    public AgileProtocolType getType() {
        return this.type == null ? AgileProtocolType.LOCAL : this.type;
    }
}
