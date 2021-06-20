package com.jeeagile.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileSessionListener implements SessionListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onStart(Session session) {
        int current = count.incrementAndGet();
        logger.debug("当前在线人数：{}", current);
    }

    @Override
    public void onStop(Session session) {
        int current = count.decrementAndGet();
        logger.debug("当前在线人数：{}", current);
    }

    @Override
    public void onExpiration(Session session) {
        int current = count.decrementAndGet();
        logger.debug("当前在线人数：{}", current);
    }
}
