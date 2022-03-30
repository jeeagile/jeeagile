package com.jeeagile.core.runner;

import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.system.util.AgileSystemUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(Integer.MIN_VALUE)
public class AgileCommandRunner implements CommandLineRunner {

    @Resource
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (AgileStringUtil.isEmpty(serverPort)) {
            serverPort = "80";
        }
        if (AgileStringUtil.isEmpty(contextPath)) {
            contextPath = "";
        }
        String host = AgileSystemUtil.getHostInfo().getAddress();
        System.out.println("*****************************************************************************");
        System.out.println("           φ(゜▽゜*)♪    " + AgileUtil.getProjectName() + "启动成功!     ლ(´ڡ`ლ)");
        System.out.println("     访问地址：");
        System.out.println("           - Local:    http://localhost:" + serverPort + "/" + contextPath);
        System.out.println("           - Network:  http://" + host + ":" + serverPort + "/" + contextPath);
        System.out.println("*****************************************************************************");
    }
}
