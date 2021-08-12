package com.jeeagile.quartz.config;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
public class AgileQuartzConfiguration {
    @Bean("agileJobFactory")
    public JobFactory agileJobFactory(ApplicationContext applicationContext) {
        AgileJobFactory agileJobFactory = new AgileJobFactory();
        agileJobFactory.setApplicationContext(applicationContext);
        return agileJobFactory;
    }

    @Bean("agileScheduler")
    @ConditionalOnMissingBean
    public SchedulerFactoryBean agileSchedulerFactory(JobFactory agileJobFactory, @Qualifier("dataSource") DataSource dataSource) throws IOException {
        SchedulerFactoryBean agileSchedulerFactory = new SchedulerFactoryBean();
        agileSchedulerFactory.setOverwriteExistingJobs(true);
        agileSchedulerFactory.setDataSource(dataSource);
        agileSchedulerFactory.setAutoStartup(true);
        agileSchedulerFactory.setJobFactory(agileJobFactory);
        agileSchedulerFactory.setQuartzProperties(agileQuartzProperties());
        return agileSchedulerFactory;
    }

    @Bean("agileQuartzProperties")
    public Properties agileQuartzProperties() throws IOException {
        PropertiesFactoryBean agileQuartzProperties = new PropertiesFactoryBean();
        agileQuartzProperties.setLocation(new ClassPathResource("/agile_quartz.properties"));
        agileQuartzProperties.afterPropertiesSet();
        return agileQuartzProperties.getObject();
    }

    public final class AgileJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}

