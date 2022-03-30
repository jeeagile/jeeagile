package com.jeeagile.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jeeagile.frame.annotation.AgileDataColumn;
import com.jeeagile.frame.annotation.AgileDataScope;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseTreeMapper;
import com.jeeagile.system.entity.AgileSysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileMapper
public interface AgileSysDeptMapper extends AgileBaseTreeMapper<AgileSysDept> {
    @AgileDataScope(dept = @AgileDataColumn(name = "id"))
    List<AgileSysDept> selectList(@Param("ew") Wrapper<AgileSysDept> queryWrapper);
}
