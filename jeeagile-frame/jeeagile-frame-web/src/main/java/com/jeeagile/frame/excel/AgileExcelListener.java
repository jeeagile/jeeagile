package com.jeeagile.frame.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.jeeagile.frame.entity.AgileModel;
import com.jeeagile.frame.service.IAgileBaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-04-02
 * @description Excel解析器基类
 */
@Slf4j
public abstract class AgileExcelListener<T extends AgileModel> extends AnalysisEventListener<T> {

    @Getter
    protected List<T> agileModelList = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        agileModelList.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("文件解析完成");
    }
}
