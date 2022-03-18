package com.jeeagile.core.runner;

import com.jeeagile.core.util.AgileUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MIN_VALUE)
public class AgileCommandRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=============================================================================");
        System.out.println("               φ(゜▽゜*)♪    " + AgileUtil.getProjectName() + "启动成功!     ლ(´ڡ`ლ)");
        System.out.println("                       欢迎使用  JeeAgile  V 2.0.0");
        System.out.println("=============================================================================");
    }
}
