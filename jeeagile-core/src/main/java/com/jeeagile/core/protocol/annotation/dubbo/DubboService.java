package com.jeeagile.core.protocol.annotation.dubbo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description dubbo 原生参数支持
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DubboService {
    /**
     * Service group, default value is empty string
     */
    String group() default "";

    /**
     * Service path, default value is empty string
     */
    String path() default "";

    /**
     * Whether to export service, default value is true
     */
    boolean export() default true;

    /**
     * Service token, default value is false
     */
    String token() default "";

    /**
     * Whether the service is deprecated, default value is false
     */
    boolean deprecated() default false;

    /**
     * Whether the service is dynamic, default value is true
     */
    boolean dynamic() default true;

    /**
     * Access log for the service, default value is ""
     */
    String accesslog() default "";

    /**
     * Maximum concurrent executes for the service, default value is 0 - no limits
     */
    int executes() default 0;

    /**
     * Whether to register the service to register center, default value is true
     */
    boolean register() default true;

    /**
     * Service weight value, default value is 0
     */
    int weight() default 0;

    /**
     * Service doc, default value is ""
     */
    String document() default "";

    /**
     * Delay time for service registration, default value is 0
     */
    int delay() default 0;


    String local() default "";

    /**
     * Service stub name, use interface name + Local if not set
     */
    String stub() default "";

    /**
     * Cluster strategy, legal values include: failover, failfast, failsafe, failback, forking
     */
    String cluster() default "";

    /**
     * How the proxy is generated, legal values include: jdk, javassist
     */
    String proxy() default "";

    /**
     * Maximum connections service provider can accept, default value is 0 - connection is shared
     */
    int connections() default 0;

    /**
     * The callback instance limit peer connection
     * <p>
     * see org.apache.dubbo.processor.Constants#DEFAULT_CALLBACK_INSTANCES
     */
    int callbacks() default 0;

    /**
     * Callback method name when connected, default value is empty string
     */
    String onconnect() default "";

    /**
     * Callback method name when disconnected, default value is empty string
     */
    String ondisconnect() default "";

    /**
     * Service owner, default value is empty string
     */
    String owner() default "";

    /**
     * Service layer, default value is empty string
     */
    String layer() default "";

    /**
     * Service invocation retry times
     */
    int retries() default 2;

    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     */
    String loadbalance() default "random";

    /**
     * Maximum active requests allowed, default value is 0
     */
    int actives() default 0;

    /**
     * Whether the async request has already been sent, the default value is false
     */
    boolean sent() default false;

    /**
     * Service mock name, use interface name + Mock if not set
     */
    String mock() default "";

    /**
     * Whether to use JSR303 validation, legal values are: true
     */
    String validation() default "";

    /**
     * Timeout value for service invocation, default value is 0
     */
    int timeout() default 120000;

    /**
     * Specify cache implementation for service invocation, legal values include: lru, threadlocal, jcache
     */
    String cache() default "";

    /**
     * Filters for service invocation
     */
    String[] filter() default {};

    /**
     * Listeners for service exporting and unexporting
     */
    String[] listener() default {};

    /**
     * Customized parameter key-value pair, for example: {key1, value1, key2, value2}
     */
    String[] parameters() default {};

    /**
     * Application spring bean name
     */
    String application() default "";

    /**
     * Module spring bean name
     */
    String module() default "";

    /**
     * Provider spring bean name
     */
    String provider() default "";

    /**
     * Protocol spring bean names
     */
    String[] protocol() default {};

    /**
     * Monitor spring bean name
     */
    String monitor() default "";

    /**
     * Registry spring bean name
     */
    String[] registry() default {};

    /**
     * Service tag name
     */
    String tag() default "";
}
