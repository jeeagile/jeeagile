package com.jeeagile.drools.kie;

import com.jeeagile.core.util.AgileStringUtil;
import lombok.Data;
import org.kie.api.io.ResourceType;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-16
 * @描述
 */
@Data
public class AgileKieRule {
    /**
     * 规则引擎名称
     */
    private String name;
    /**
     * 规则文件路径
     */
    private String path;
    /**
     * 规则文件类型 drl、xls、xlsx
     */
    private ResourceType type = ResourceType.DRL;
    /**
     * 规则文件内容
     */
    private String content;

    public String getPath() {
        if (AgileStringUtil.isEmpty(this.path)) {
            return this.name + "." + this.type.getDefaultExtension();
        } else {
            return this.path;
        }
    }

    public ResourceType getType() {
        if (this.type == null) {
            if (AgileStringUtil.isEmpty(this.path)) {
                return ResourceType.determineResourceType(this.name);
            } else {
                return ResourceType.determineResourceType(this.path);
            }
        } else {
            return this.type;
        }
    }
}
