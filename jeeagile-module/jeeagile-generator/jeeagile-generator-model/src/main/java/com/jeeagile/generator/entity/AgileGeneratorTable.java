package com.jeeagile.generator.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileGeneratorTable extends AgileBaseModel<AgileGeneratorTable> {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表描述
     */
    private String tableComment;
    /**
     * 表类型（crud单表 tree树表）
     */
    private String tableType;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 生成包路径
     */
    private String packageName;
    /**
     * 实体类名称
     */
    private String className;
    /**
     * 生成模块名
     */
    private String moduleName;
    /**
     * 生成业务名
     */
    private String businessName;
    /**
     * 生成功能名
     */
    private String functionName;
    /**
     * 生成功能作者
     */
    private String functionAuthor;
    /**
     * 上级菜单名称字段
     */
    private String parentMenuId;
    /**
     * 树名称字段
     */
    private String treeName;
    /**
     * 树编码字段
     */
    private String treeCode;
    /**
     * 树父编码字段
     */
    private String treeParentCode;
    /**
     * 备注
     */
    private String remark;
}
