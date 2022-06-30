package com.jeeagile.frame.plugins.datascope;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileSpringUtil;
import com.jeeagile.frame.plugins.helper.AgileDataScopeHelper;
import com.jeeagile.frame.plugins.datascope.property.AgileDataScopeProperty;
import com.jeeagile.frame.plugins.parser.AgileSqlParserSupport;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JeeAgile
 * @date 2022-03-21
 * @description 数据权限处理器
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AgileDataScopeInterceptor extends AgileSqlParserSupport implements InnerInterceptor {
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) return;
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId(), parameter));
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj, Object parameter) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody, (String) obj, parameter);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, (String) obj, parameter));
        }
    }

    /**
     * 设置 where 条件
     *
     * @param plainSelect       查询对象
     * @param mappedStatementId 执行方法
     */
    protected void setWhere(PlainSelect plainSelect, String mappedStatementId, Object parameter) {
        AgileDataScopeProperty agileDataScopeProperty = AgileDataScopeHelper.getAgileDataScope(mappedStatementId);
        Expression sqlSegment = null;
        if (agileDataScopeProperty != null) {
            if (agileDataScopeProperty.getType().equals("inner")) {
                sqlSegment = this.getInnerExpression(agileDataScopeProperty);
            } else {
                AgileDataScopeHandler agileDataScopeHandler = AgileSpringUtil.getBean(AgileDataScopeHandler.class);
                if (agileDataScopeHandler != null) {
                    sqlSegment = agileDataScopeHandler.getSqlSegment(plainSelect.getWhere(), mappedStatementId, parameter);
                }
            }
            if (null != sqlSegment) {
                if (plainSelect.getWhere() != null) {
                    plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), sqlSegment));
                } else {
                    plainSelect.setWhere(new Parenthesis(sqlSegment));
                }
            }
        }
    }

    private Expression getInnerExpression(AgileDataScopeProperty agileDataScopeProperty) {
        AgileBaseUser agileBaseUser = AgileSecurityUtil.getUserData();
        if (agileBaseUser == null && AgileStringUtil.isEmpty(agileBaseUser.getUserId())) {
            throw new AgileAuthException("获取用户信息发生异常!");
        }
        IAgileUserDetailsService agileUserDetailsService = AgileSpringUtil.getBean(IAgileUserDetailsService.class);
        if (agileBaseUser.isSuperAdmin() || agileBaseUser.getUserName().equals(agileBaseUser.getTenantCode())) {
            return null;
        }
        List<String> dataScopeList = agileUserDetailsService.getUserDataScope(agileBaseUser);
        Parenthesis parenthesis = new Parenthesis();
        if (AgileCollectionUtil.isNotEmpty(dataScopeList)) {
            if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_01)) {
                return null;
            }
            InExpression deptCondition = null;
            if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_02)
                    || dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_03)
                    || dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_05)) {
                Set<String> deptList = getUserDeptList(agileUserDetailsService, agileBaseUser, dataScopeList);
                ItemsList itemsList = new ExpressionList(deptList.stream().map(StringValue::new).collect(Collectors.toList()));
                deptCondition = new InExpression(new Column(agileDataScopeProperty.getDeptColumn().getDotAliasName()), itemsList);
            }
            EqualsTo userCondition = null;
            if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_04)) {
                userCondition = new EqualsTo();
                userCondition.setLeftExpression(new Column(agileDataScopeProperty.getUserColumn().getDotAliasName()));
                userCondition.setRightExpression(new StringValue(agileBaseUser.getUserId()));
            }
            if (deptCondition != null && userCondition != null) {
                parenthesis.setExpression(new OrExpression(deptCondition, userCondition));
            } else if (deptCondition != null) {
                parenthesis.setExpression(deptCondition);
            } else if (userCondition != null) {
                parenthesis.setExpression(userCondition);
            } else {
                parenthesis.setExpression(new EqualsTo(new Column("1"), new LongValue(0)));
            }
        } else {
            parenthesis.setExpression(new EqualsTo(new Column("1"), new LongValue(0)));
        }
        return parenthesis;
    }

    private Set<String> getUserDeptList(IAgileUserDetailsService agileUserDetailsService, AgileBaseUser agileBaseUser, List<String> dataScopeList) {
        Set<String> userDeptList = new HashSet<>();
        if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_02)) {
            userDeptList.add(agileBaseUser.getDeptId());
        }
        if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_03)) {
            userDeptList.add(agileBaseUser.getDeptId());
            userDeptList.addAll(agileUserDetailsService.getUserDeptScopeList(agileBaseUser, AgileConstants.AGILE_DATA_SCOPE_03));
        }
        if (dataScopeList.contains(AgileConstants.AGILE_DATA_SCOPE_05)) {
            userDeptList.addAll(agileUserDetailsService.getUserDeptScopeList(agileBaseUser, AgileConstants.AGILE_DATA_SCOPE_05));
        }
        //防止没有分配任何部门权限
        if (userDeptList.isEmpty()) {
            userDeptList.add("NULL");
        }
        return userDeptList;
    }

}
