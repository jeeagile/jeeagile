package com.jeeagile.drools.vo;

import com.jeeagile.drools.entity.AgileDroolsModelField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 数据对象 字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgileDroolsModelFieldInfo extends AgileDroolsModelField {
    private AgileDroolsModelInfo droolsModelInfo;
}
