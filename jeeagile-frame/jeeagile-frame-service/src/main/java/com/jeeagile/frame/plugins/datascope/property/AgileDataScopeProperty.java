package com.jeeagile.frame.plugins.datascope.property;

import com.jeeagile.frame.annotation.AgileDataColumn;
import com.jeeagile.frame.annotation.AgileDataScope;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AgileDataScopeProperty {
    private String type;
    private AgileDataColumnProperty userColumn;
    private AgileDataColumnProperty deptColumn;
    private List<AgileDataColumnProperty> otherColumns;

    public AgileDataScopeProperty(AgileDataScope agileDataScope) {
        this.type = agileDataScope.type();
        this.userColumn = new AgileDataColumnProperty(agileDataScope.user());
        this.deptColumn = new AgileDataColumnProperty(agileDataScope.dept());
        if (agileDataScope.other() != null && agileDataScope.other().length > 0) {
            this.otherColumns = Arrays.stream(agileDataScope.other()).map((agileDataColumn) -> new AgileDataColumnProperty(agileDataColumn)).collect(Collectors.toList());
        }
    }
}
