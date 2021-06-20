package com.jeeagile.frame.autoconfigure;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@AutoConfigureOrder(-100)
public class AgileTransactionAutoConfig {

    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.jeeagile..*.service..*.*(..))";
    /**
     * 注入事务管理器
     */
    @Autowired
    private TransactionManager agileTransactionManager;

    /**
     * 配置事务拦截器
     */
    public TransactionInterceptor agileAdvice() {
        RuleBasedTransactionAttribute txAttrRequired = new RuleBasedTransactionAttribute();
        txAttrRequired.setName("REQUIRED事务");
        //设置事务传播机制，默认是PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务
        txAttrRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置异常回滚为Exception  默认是RuntimeException
        List<RollbackRuleAttribute> rollbackRuleAttributes = new ArrayList<>();
        rollbackRuleAttributes.add(new RollbackRuleAttribute(Exception.class));
        txAttrRequired.setRollbackRules(rollbackRuleAttributes);

        RuleBasedTransactionAttribute txAttrRequiredReadOnly = new RuleBasedTransactionAttribute();
        txAttrRequiredReadOnly.setName("SUPPORTS事务");
        //设置事务传播机制，PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行
        txAttrRequiredReadOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        //设置异常回滚为Exception  默认是RuntimeException
        txAttrRequiredReadOnly.setRollbackRules(rollbackRuleAttributes);
        txAttrRequiredReadOnly.setReadOnly(true);

        /*事务管理规则，声明具备事务管理的方法名*/
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        //方法名规则限制，必须以下列开头才会加入事务管理当中
        source.addTransactionalMethod("add*", txAttrRequired);
        source.addTransactionalMethod("save*", txAttrRequired);
        source.addTransactionalMethod("create*", txAttrRequired);
        source.addTransactionalMethod("insert*", txAttrRequired);
        source.addTransactionalMethod("submit*", txAttrRequired);
        source.addTransactionalMethod("del*", txAttrRequired);
        source.addTransactionalMethod("remove*", txAttrRequired);
        source.addTransactionalMethod("update*", txAttrRequired);
        source.addTransactionalMethod("set*", txAttrRequired);
        source.addTransactionalMethod("exec*", txAttrRequired);
        source.addTransactionalMethod("change*", txAttrRequired);
        source.addTransactionalMethod("import*", txAttrRequired);

        //对于查询方法，根据实际情况添加事务管理 可能存在查询多个数据时，已查询出来的数据刚好被改变的情况
        source.addTransactionalMethod("get*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("select*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("query*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("find*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("list*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("count*", txAttrRequiredReadOnly);
        source.addTransactionalMethod("is*", txAttrRequiredReadOnly);
        return new TransactionInterceptor(agileTransactionManager, source);
    }

    /**
     * 设置切面
     */
    @Bean
    public Advisor agileAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, agileAdvice());
    }
}
