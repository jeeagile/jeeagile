package com.jeeagile.frame.mapper.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeeagile.frame.annotation.AgileDataScope;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.frame.entity.system.AgileSysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileMapper
public interface AgileSysUserMapper extends AgileBaseMapper<AgileSysUser> {
    @AgileDataScope
    <P extends IPage<AgileSysUser>> P selectPage(P page, @Param("ew") Wrapper<AgileSysUser> queryWrapper);
}
