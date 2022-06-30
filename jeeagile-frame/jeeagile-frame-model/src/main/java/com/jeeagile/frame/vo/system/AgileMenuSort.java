package com.jeeagile.frame.vo.system;

import com.jeeagile.frame.vo.AgileBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileMenuSort extends AgileBaseVo {
    private String id;
    private Integer menuSort;
}
