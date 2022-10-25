package com.jeeagile.generator.service;


import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.generator.entity.AgileGeneratorTable;

import java.util.List;
import java.util.Map;


/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
public interface IAgileGeneratorTableService extends IAgileBaseService<AgileGeneratorTable> {
    /**
     * 分页查询代码生成列表
     */
    AgilePage<AgileGeneratorTable> selectTablePage(AgilePageable<AgileGeneratorTable> agilePageable);

    /**
     * 分页查询数据库表列表
     */
    AgilePage<AgileGeneratorTable> selectDbTablePage(AgilePageable<AgileGeneratorTable> agilePageable);

    /**
     * 导入代码生成表信息
     */
    boolean importTable(List<String> tableNameList);

    /**
     * 查看代码生成表信息
     */
    AgileGeneratorTable selectTableInfoById(String agileGeneratorTableId);

    /**
     * 更新代码生成表信息
     */
    boolean updateTableInfoById(AgileGeneratorTable agileGeneratorTable);

    /**
     * 删除代码表生成信息
     */
    boolean deleteTableById(String agileGeneratorTableId);

    /**
     * 同步代码表生成信息
     */
    boolean syncTableById(String agileGeneratorTableId);

    /**
     * 预览生成代码
     */
    Map<String,String> previewCode(String agileGeneratorTableId);

    /**
     * 下载生成代码
     */
    byte[] downloadCode(List<String> agileGeneratorTableIdList);
}
