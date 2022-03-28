package com.jeeagile.frame.plugins.datascope.property;

import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDataColumn;
import lombok.Getter;

@Getter
public class AgileDataColumnProperty {
    private String alias;
    private String name;

    public AgileDataColumnProperty(AgileDataColumn column) {
        this.alias = column.alias();
        this.name = column.name();
    }

    public String getDotAliasName() {
        return AgileStringUtil.isNotEmpty(alias) ? this.alias + "." + this.name : this.name;
    }
}
