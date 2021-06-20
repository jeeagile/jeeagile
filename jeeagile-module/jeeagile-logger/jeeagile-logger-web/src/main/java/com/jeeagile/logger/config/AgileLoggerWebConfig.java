package com.jeeagile.logger.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@EnableAsync
@Configurable
public class AgileLoggerWebConfig {
    @Bean("AgileAsyncTask")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(80);
        threadPoolTaskExecutor.setQueueCapacity(30);
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        threadPoolTaskExecutor.setThreadNamePrefix("AgileAsyncTask");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
