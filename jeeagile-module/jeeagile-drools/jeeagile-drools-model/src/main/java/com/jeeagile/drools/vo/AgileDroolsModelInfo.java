package com.jeeagile.drools.vo;

import com.jeeagile.drools.entity.AgileDroolsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 数据对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgileDroolsModelInfo extends AgileDroolsModel {
    private List<AgileDroolsModelFieldInfo> droolsModelFieldList = new ArrayList<>();
}
