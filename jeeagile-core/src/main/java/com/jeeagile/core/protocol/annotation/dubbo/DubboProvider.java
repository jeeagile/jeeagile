package com.jeeagile.core.protocol.annotation.dubbo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description dubbo 原生参数支持
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface DubboProvider {
    /**
     * Service group, default value is empty string
     */
    String group() default "";

    /**
     * Service target URL for direct invocation, if this is specified, then registry center takes no effect.
     */
    String url() default "";

    /**
     * Client transport type, default value is "netty"
     */
    String client() default "";

    /**
     * Whether to enable generic invocation, default value is false
     */
    boolean generic() default false;

    /**
     * Check if service provider is available during boot up, default value is true
     */
    boolean check() default true;

    /**
     * Whether eager initialize the reference bean when all properties are set, default value is false
     */
    boolean init() default false;

    /**
     * Whether to make connection when the client is created, the default value is false
     */
    boolean lazy() default false;

    /**
     * Export an stub service for event dispatch, default value is false.
     * <p>
     * see org.apache.dubbo.processor.Constants#STUB_EVENT_METHODS_KEY
     */
    boolean stubevent() default false;

    /**
     * Whether to reconnect if connection is lost, if not specify, reconnect is enabled by default, and the interval
     * for retry connecting is 2000 ms
     * <p>
     * see org.apache.dubbo.remoting.Constants#DEFAULT_RECONNECT_PERIOD
     */
    String reconnect() default "";

    /**
     * Whether to stick to the same node in the cluster, the default value is false
     * <p>
     * see Constants#DEFAULT_CLUSTER_STICKY
     */
    boolean sticky() default false;

    /**
     * How the proxy is generated, legal values include: jdk, javassist
     */
    String proxy() default "";

    /**
     * Service stub name, use interface name + Local if not set
     */
    String stub() default "";

    /**
     * Cluster strategy, legal values include: failover, failfast, failsafe, failback, forking
     */
    String cluster() default "";

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
     * <p>
     * see Constants#DEFAULT_RETRIES
     */
    int retries() default 2;

    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     * <p>
     * see Constants#DEFAULT_LOADBALANCE
     */
    String loadbalance() default "";

    /**
     * Whether to enable async invocation, default value is false
     */
    boolean async() default false;

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
    int timeout() default 0;

    /**
     * Specify cache implementation for service invocation, legal values include: lru, threadlocal, jcache
     */
    String cache() default "";

    /**
     * Filters for service invocation
     * <p>
     * see Filter
     */
    String[] filter() default {};

    /**
     * Listeners for service exporting and unexporting
     * <p>
     * see ExporterListener
     */
    String[] listener() default {};

    /**
     * Customized parameter key-value pair, for example: {key1, value1, key2, value2}
     */
    String[] parameters() default {};

    /**
     * Application associated name
     */
    String application() default "";

    /**
     * Module associated name
     */
    String module() default "";

    /**
     * Consumer associated name
     */
    String consumer() default "";

    /**
     * Monitor associated name
     */
    String monitor() default "";

    /**
     * Registry associated name
     */
    String[] registry() default {};

    /**
     * The communication protocol of Dubbo Service
     *
     * @return the default value is ""
     */
    String protocol() default "";

    /**
     * Service tag name
     */
    String tag() default "";

    /**
     * Service merger
     */
    String merger() default "";

    /**
     * The id
     *
     * @return default value is empty
     */
    String id() default "";

    /**
     * @return The service names that the Dubbo interface subscribed
     */
    String[] services() default {};
}
