package com.jeeagile.frame.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jeeagile.core.util.AgileStringUtil;
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
public abstract class AgileModel<T extends AgileModel<T>> extends Model<T> {
    /**
     * 判断主键是否为空
     *
     * @return
     */
    public boolean isNotEmptyPk() {
        return AgileStringUtil.isNotEmpty(this.pkVal());
    }

    /**
     * 判断主键是否为空
     *
     * @return
     */
    public boolean isEmptyPk() {
        return AgileStringUtil.isEmpty(this.pkVal());
    }
}
