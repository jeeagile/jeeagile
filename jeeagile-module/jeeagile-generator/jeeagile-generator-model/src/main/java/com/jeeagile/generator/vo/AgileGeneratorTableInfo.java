package com.jeeagile.generator.vo;

import com.jeeagile.generator.entity.AgileGeneratorTable;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileGeneratorTableInfo extends AgileGeneratorTable {
    /**
     * 表字段信息
     */
    List<AgileGeneratorTableColumn> agileGeneratorTableColumnList;
}
