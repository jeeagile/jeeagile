package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysPost;
import com.jeeagile.system.service.IAgileSysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 岗位管理 前端控制器
 */
@RestController
@RequestMapping("/system/post")
@Api(value = "岗位管理", tags = "岗位管理")
public class AgileSysPostController extends AgileBaseController {

    @AgileReference
    private IAgileSysPostService agileSysPostService;

    @PostMapping(value = "/selectPage")
    @ApiOperation(value = "分页查询岗位管理列表", notes = "分页查询岗位管理列表")
    @AgileLogger(title = "查询岗位列表", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysPost>> selectPostPage(@RequestBody AgilePageable<AgileSysPost> agilePageable) {
        return this.success(agileSysPostService.selectPostPage(agilePageable));
    }

    @PostMapping(value = "/selectList")
    @ApiOperation(value = "查询岗位列表", notes = "查询岗位列表")
    @AgileLogger(title = "查看岗位列表", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysPost>> selectPostList(@RequestBody AgileSysPost agileSysPost) {
        return this.success(agileSysPostService.selectPostList(agileSysPost));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据岗位ID获取岗位详细信息", notes = "根据岗位ID获取岗位详细信息")
    @AgileLogger(title = "查看岗位信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysPost> detail(@SingleRequestBody String postId) {
        return this.success(agileSysPostService.selectPostById(postId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增岗位", notes = "新增岗位")
    @AgileLogger(title = "新增岗位", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:post:add")
    public AgileResult<AgileSysPost> add(@RequestBody AgileSysPost agileSysPost) {
        return this.success(agileSysPostService.savePost(agileSysPost));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改岗位", notes = "修改岗位")
    @AgileLogger(title = "修改岗位", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:post:update")
    public AgileResult<String> update(@RequestBody AgileSysPost agileSysPost) {
        agileSysPostService.updatePostById(agileSysPost);
        return this.success("岗位信息更新成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除岗位", notes = "删除岗位")
    @AgileLogger(title = "删除岗位", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:post:delete")
    public AgileResult<String> delete(@SingleRequestBody String postId) {
        agileSysPostService.deletePostById(postId);
        return this.success("岗位信息删除成功！");
    }
}
