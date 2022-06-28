package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.entity.system.AgileSysDept;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.entity.system.AgileSysUserPost;
import com.jeeagile.frame.entity.system.AgileSysUserRole;
import com.jeeagile.frame.mapper.system.AgileSysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeeagile.core.constants.AgileConstants.SYS_USER_SEX;
import static com.jeeagile.core.constants.AgileConstants.SYS_USER_STATUS;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserServiceImpl extends AgileBaseServiceImpl<AgileSysUserMapper, AgileSysUser> implements IAgileSysUserService {

    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysPostService agileSysPostService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;
    @Autowired
    private IAgileSysConfigService agileSysConfigService;
    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;

    @Override
    public Object initData() {
        Map initData = new HashMap();
        initData.put("deptList", agileSysDeptService.selectList());
        initData.put("postList", agileSysPostService.selectList());
        initData.put("roleList", agileSysRoleService.selectList());
        return initData;
    }

    @Override
    public LambdaQueryWrapper<AgileSysUser> queryWrapper(AgileSysUser agileSysUser) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                lambdaQueryWrapper.like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserEmail())) {
                lambdaQueryWrapper.like(AgileSysUser::getUserEmail, agileSysUser.getUserEmail());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserPhone())) {
                lambdaQueryWrapper.like(AgileSysUser::getUserPhone, agileSysUser.getUserPhone());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getDeptId())) {
                lambdaQueryWrapper.eq(AgileSysUser::getDeptId, agileSysUser.getDeptId());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserStatus())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserStatus, agileSysUser.getUserStatus());
            }
        }
        if (AgileTenantUtil.isTenantEnable()) {
            lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
            lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileSecurityUtil.getTenantCode());
        } else {
            lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
        }
        lambdaQueryWrapper.orderByAsc(AgileSysUser::getUserSort);
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysUser> selectExportData(AgileSysUser agileSysUser) {
        List<AgileSysUser> agileSysUserList = this.selectList(agileSysUser);
        agileSysUserList.forEach(item -> {
            item.setUserSex(agileSysDictDataService.getSysDictLabel(SYS_USER_SEX, item.getUserSex()));
            item.setUserStatus(agileSysDictDataService.getSysDictLabel(SYS_USER_STATUS, item.getUserStatus()));
            AgileSysDept agileSysDept = agileSysDeptService.getById(item.getDeptId());
            if (agileSysDept != null && agileSysDept.isNotEmptyPk()) {
                item.setDeptName(agileSysDept.getDeptName());
            }
        });
        return agileSysUserList;
    }

    @Override
    public Object importData(List<AgileSysUser> agileModelList) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = agileSysConfigService.getDefaultPassword();
        for (AgileSysUser agileSysUser : agileModelList) {
            try {
                if (this.userNameExist(agileSysUser.getUserName())) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、用户 " + agileSysUser.getUserName() + " 已存在！");
                    continue;
                }
                agileSysUser.setUserSex(agileSysDictDataService.getSysDictValue(SYS_USER_SEX, agileSysUser.getUserSex()));
                agileSysUser.setUserStatus(agileSysDictDataService.getSysDictValue(SYS_USER_STATUS, agileSysUser.getUserStatus()));
                agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(password));
                String deptId = getDeptId(agileSysUser.getDeptName());
                if (AgileStringUtil.isEmpty(deptId)) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、用户 " + agileSysUser.getUserName() + " 部门不存在！");
                    continue;
                }
                agileSysUser.setDeptId(deptId);
                if (this.save(agileSysUser)) {
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、用户 " + agileSysUser.getUserName() + " 导入成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、用户 " + agileSysUser.getUserName() + " 导入失败！");
                }
            } catch (Exception ex) {
                failureNum++;
                failureMsg.append("<br/>" + failureNum + "、用户 " + agileSysUser.getUserName() + " 导入失败！");
            }

        }
        if (failureNum > 0) {
            failureMsg.insert(0, "抱歉，共 " + failureNum + " 条数据导入失败！，错误数据如下：");
            return failureMsg.toString();
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            return successMsg.toString();
        }
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUser::getUserName, userName);
        AgileSysUser agileSysUser = this.getOne(lambdaQueryWrapper);
        if (agileSysUser != null && agileSysUser.isNotEmptyPk()) {
            return true;
        } else {
            return false;
        }
    }

    private String getDeptId(String deptName) {
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDept::getDeptName, deptName);
        AgileSysDept agileSysDept = agileSysDeptService.getOne(lambdaQueryWrapper);
        if (agileSysDept != null && agileSysDept.isNotEmptyPk()) {
            return agileSysDept.getId();
        } else {
            return null;
        }
    }

    @Override
    public AgileSysUser selectModel(Serializable userId) {
        AgileSysUser agileSysUser = this.getById(userId);
        agileSysUser.setRoleIdList(this.selectUserRoleIdList(userId));
        agileSysUser.setPostIdList(this.selectUserPostIdList(userId));
        return agileSysUser;
    }

    @Override
    public AgileSysUser saveModel(AgileSysUser agileSysUser) {
        String userPwd = agileSysUser.getUserPwd();
        if (AgileStringUtil.isEmpty(userPwd)) {
            userPwd = agileSysConfigService.getDefaultPassword();
        }
        agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(userPwd));
        this.validateData(agileSysUser);
        this.save(agileSysUser);
        this.saveUserRole(agileSysUser);
        this.saveUserPost(agileSysUser);
        return agileSysUser;
    }

    @Override
    public boolean updateModel(AgileSysUser agileSysUser) {
        //防止修改用户信息将用户密码进行修改
        agileSysUser.setUserPwd(null);
        this.validateData(agileSysUser);
        this.updateById(agileSysUser);
        this.deleteUserPost(agileSysUser.getId());
        this.deleteUserRole(agileSysUser.getId());
        this.saveUserRole(agileSysUser);
        return this.saveUserPost(agileSysUser);
    }

    @Override
    public boolean deleteModel(Serializable userId) {
        this.deleteUserPost(userId);
        this.deleteUserRole(userId);
        return this.removeById(userId);
    }

    @Override
    public boolean resetUserPassword(Serializable userId, String password) {
        if (AgileStringUtil.isEmpty(password)) {
            password = agileSysConfigService.getDefaultPassword();
        }
        password = AgileSecurityUtil.encryptPassword(password);
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(userId.toString());
        agileSysUser.setUserPwd(password);
        return this.updateById(agileSysUser);
    }

    @Override
    public boolean changeUserStatus(Serializable userId, String userStatus) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(userId.toString());
        agileSysUser.setUserStatus(userStatus);
        return this.updateById(agileSysUser);
    }

    /**
     * 查询用户已分配角色ID
     *
     * @param userId
     * @return
     */
    private List<String> selectUserRoleIdList(Serializable userId) {
        LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
        lambdaQueryWrapper.select(AgileSysUserRole::getRoleId);
        List<AgileSysUserRole> userRoleList = agileSysUserRoleService.list(lambdaQueryWrapper);
        List<String> userRoleIdList = new ArrayList<>();
        userRoleList.forEach(sysUserRole -> userRoleIdList.add(sysUserRole.getRoleId()));
        return userRoleIdList;
    }

    /**
     * 查询用户已分配岗位ID
     *
     * @param userId
     * @return
     */
    private List<String> selectUserPostIdList(Serializable userId) {
        LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
        lambdaQueryWrapper.select(AgileSysUserPost::getPostId);
        List<AgileSysUserPost> userPostList = agileSysUserPostService.list(lambdaQueryWrapper);
        List<String> userPostIdList = new ArrayList<>();
        userPostList.forEach(sysUserRole -> userPostIdList.add(sysUserRole.getPostId()));
        return userPostIdList;
    }

    /**
     * 删除用户已分配岗位
     *
     * @param userId
     * @return
     */
    private boolean deleteUserPost(Serializable userId) {
        if (AgileStringUtil.isNotEmpty(userId)) {
            LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
            return agileSysUserPostService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 删除用户已分配角色
     *
     * @param userId
     * @return
     */
    private boolean deleteUserRole(Serializable userId) {
        if (AgileStringUtil.isNotEmpty(userId)) {
            LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
            return agileSysUserRoleService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 用户名不能与已存在的数据重复
     */
    private void validateData(AgileSysUser agileSysUser) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysUser.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysUser::getId, agileSysUser.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("用户名不能与已存在的数据重复！");
        }
    }


    /**
     * 保存用户岗位
     *
     * @param agileSysUser
     * @return
     */
    private boolean saveUserPost(AgileSysUser agileSysUser) {
        if (AgileCollectionUtil.isNotEmpty(agileSysUser.getPostIdList())) {
            List<AgileSysUserPost> agileSysUserPostList = new ArrayList<>();
            for (String postId : agileSysUser.getPostIdList()) {
                AgileSysUserPost agileSysUserPost = new AgileSysUserPost();
                agileSysUserPost.setUserId(agileSysUser.getId());
                agileSysUserPost.setPostId(postId);
                agileSysUserPostList.add(agileSysUserPost);
            }
            return agileSysUserPostService.saveBatch(agileSysUserPostList);
        } else {
            return true;
        }
    }


    /**
     * 保存用户已分配的角色
     */
    private boolean saveUserRole(AgileSysUser agileSysUser) {
        if (AgileCollectionUtil.isNotEmpty(agileSysUser.getRoleIdList())) {
            List<AgileSysUserRole> agileSysUserRoleList = new ArrayList<>();
            for (String roleId : agileSysUser.getRoleIdList()) {
                AgileSysUserRole agileSysUserRole = new AgileSysUserRole();
                agileSysUserRole.setUserId(agileSysUser.getId());
                agileSysUserRole.setRoleId(roleId);
                agileSysUserRoleList.add(agileSysUserRole);
            }
            return agileSysUserRoleService.saveBatch(agileSysUserRoleList);
        } else {
            return true;
        }
    }
}
