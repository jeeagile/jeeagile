package com.jeeagile.frame.plugins.tenant;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.AgileTenantModel;
import com.jeeagile.frame.properties.AgileTenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.Set;

@Slf4j
@AllArgsConstructor
public class AgileTenantLineHandler implements TenantLineHandler {
    private AgileTenantProperties agileTenantProperties;

    @Override
    public Expression getTenantId() {
        try {
            String tenantId = AgileSecurityUtil.getTenantId();
            if (AgileStringUtil.isNotEmpty(tenantId)) {
                return new StringValue(tenantId);
            }
        } catch (Exception ex) {
            log.warn("获取租户异常", ex);
        }
        return null;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 如果实体类继承 AgileTenantModel 类，则任务为多租户表，不进行忽略
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (tableInfo != null && AgileTenantModel.class.isAssignableFrom(tableInfo.getEntityType())) {
            return false;
        }
        Set<String> tenantTables = agileTenantProperties.getTables();
        if (AgileCollectionUtil.isNotEmpty(tenantTables)) {
            return !tenantTables.stream().anyMatch((i) -> i.equalsIgnoreCase(tableName));
        }
        return true;
    }
}
