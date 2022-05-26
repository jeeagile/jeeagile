package com.jeeagile.frame.mapper.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.jeeagile.frame.annotation.AgileDataColumn;
import com.jeeagile.frame.annotation.AgileDataScope;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileTreeMapper;
import com.jeeagile.frame.entity.system.AgileSysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileMapper
public interface AgileSysDeptMapper extends AgileTreeMapper<AgileSysDept> {
    @AgileDataScope(dept = @AgileDataColumn(name = "id"))
    List<AgileSysDept> selectList(@Param("ew") Wrapper<AgileSysDept> queryWrapper);
}
