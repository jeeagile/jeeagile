package com.jeeagile.frame.plugins.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.jeeagile.frame.mapper.AgileTreeMapper;
import com.jeeagile.frame.plugins.method.AgileTreeCountChild;
import com.jeeagile.frame.plugins.method.AgileTreeSelectChild;

import java.util.List;


public class AgileCustomSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        if (AgileTreeMapper.class.isAssignableFrom(mapperClass)) {
            methodList.add(new AgileTreeSelectChild());
            methodList.add(new AgileTreeCountChild());
        }
        return methodList;
    }
}
