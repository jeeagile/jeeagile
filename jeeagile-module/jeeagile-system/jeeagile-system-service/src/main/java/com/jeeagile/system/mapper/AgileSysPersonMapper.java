package com.jeeagile.system.mapper;

import com.jeeagile.frame.annotation.AgileMapperScan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileMapperScan
public interface AgileSysPersonMapper {
    /**
     * 根据用户ID获取已分配的角色名称
     */
    @Select(" select distinct r.role_name" +
            " from agile_sys_role r" +
            " left join agile_sys_user_role ur on r.id = ur.role_id" +
            " where r.role_status = '0' and ur.user_id = #{userId}")
    List<String> getRoleNameByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID获取已分配的岗位名称
     */
    @Select(" select distinct r.post_name" +
            " from agile_sys_post r" +
            " left join agile_sys_user_post ur on r.id = ur.post_id" +
            " where r.post_status = '0' and ur.user_id = #{userId}")
    List<String> getPostNameByUserId(@Param("userId") String userId);
}
