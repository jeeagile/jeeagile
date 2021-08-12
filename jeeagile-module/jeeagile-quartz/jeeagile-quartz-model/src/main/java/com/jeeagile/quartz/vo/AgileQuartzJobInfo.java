package com.jeeagile.quartz.vo;

import com.jeeagile.quartz.entity.AgileQuartzJob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileQuartzJobInfo extends AgileQuartzJob {
    private Date nextTime;
}
