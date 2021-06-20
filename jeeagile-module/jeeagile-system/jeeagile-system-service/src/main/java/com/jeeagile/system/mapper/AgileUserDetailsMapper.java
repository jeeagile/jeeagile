package com.jeeagile.system.mapper;

import com.jeeagile.frame.annotation.AgileMapperScan;
import com.jeeagile.system.entity.AgileSysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileMapperScan
public interface AgileUserDetailsMapper {
    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    @Select(" select distinct m.menu_perm" +
            " from agile_sys_menu m" +
            " left join agile_sys_role_menu rm on m.id = rm.menu_id" +
            " left join agile_sys_user_role ur on rm.role_id = ur.role_id" +
            " left join agile_sys_role r on ur.role_id=r.id" +
            " where m.menu_status = '0' and r.role_status = '0'" +
            " and  m.menu_perm is not null and ur.user_id = #{userId}")
    List<String> getUserPermByUserId(@Param("userId") String userId);

    @Select(" select distinct r.role_code" +
            " from agile_sys_role r" +
            " left join agile_sys_user_role ur on ur.role_id=r.id" +
            " where r.role_status = '0'" +
            " and ur.user_id = #{userId}")
    List<String> getUserRoleByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID获取用户菜单
     *
     * @param userId
     * @return
     */
    @Select(" select distinct m.id, m.parent_id, m.menu_name, m.menu_path, m.menu_comp, m.menu_frame," +
            " m.menu_visible, m.menu_status, m.menu_perm,  m.menu_type, m.menu_icon, m.menu_sort" +
            " from agile_sys_menu m" +
            " left join agile_sys_role_menu rm on m.id = rm.menu_id" +
            " left join agile_sys_user_role ur on rm.role_id = ur.role_id" +
            " left join agile_sys_role ro on ur.role_id = ro.id" +
            " left join agile_sys_user u on ur.user_id = u.id" +
            " where u.id = #{userId} and m.menu_type in ('M', 'C') " +
            " and m.menu_status = 0  AND ro.role_status = 0" +
            " order by m.parent_id, m.menu_sort")
    List<AgileSysMenu> getUserMenuByUserId(@Param("userId") String userId);


    @Select(" select distinct m.id, m.parent_id, m.menu_name, m.menu_path, m.menu_comp,m.menu_frame, " +
            " m.menu_visible, m.menu_status, m.menu_perm,  m.menu_type, m.menu_icon, m.menu_sort" +
            " from agile_sys_menu m where m.menu_type in ('M', 'C') and m.menu_status = 0" +
            " order by m.parent_id, m.menu_sort")
    List<AgileSysMenu> getUserMenuAll();
}
