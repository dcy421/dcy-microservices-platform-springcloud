package com.dcy.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.api.dto.SysUserInfoGroupDTO;
import com.dcy.api.dto.SysUserInfoRoleDTO;
import com.dcy.api.dto.UserSearchDTO;
import com.dcy.api.model.SysUserInfo;
import com.dcy.api.service.SysUserInfoRemoteService;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysUserInfoService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/user")
@Api(value = "SysUserInfoController", tags = {"用户操作接口"})
public class SysUserInfoController extends BaseController<ISysUserInfoService, SysUserInfo> implements SysUserInfoRemoteService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseData<Boolean> save(@RequestBody SysUserInfo sysUserInfo) {
        // 赋值加密密码
        sysUserInfo.setPassword("{bcrypt}" + passwordEncoder.encode(sysUserInfo.getPassword()));
        return super.save(sysUserInfo);
    }

    @GetMapping(value = "/pageListByDto")
    public ResponseData<IPage<SysUserInfo>> pageListByDto(UserSearchDTO userSearchDTO) {
        return ResponseData.success(baseService.pageListByDto(userSearchDTO));
    }

    @ApiOperation(value = "根据用户名获取用户信息", notes = "根据用户名获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getUserInfoByUsername")
    public ResponseData<SysUserInfo> getUserInfoByUsername(@RequestParam(value = "username") String username) {
        return ResponseData.success(baseService.getOne(new LambdaQueryWrapper<SysUserInfo>().eq(SysUserInfo::getUsername, username)));
    }

    @ApiOperation(value = "根据用户ID获取权限", notes = "根据用户ID获取权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAllPermissionByUserId")
    @Override
    public ResponseData<Set<String>> getAllPermissionByUserId(@RequestParam(value = "userId") String userId) {
        // 查询权限
        Set<String> permissionSet = baseService.getPermissionListByUserId(userId);
        Set<String> roleSet = baseService.getAuthRoleSetByUserId(userId);
        Set<String> moduleSet = baseService.getModuleListByUserId(userId);
        // 合并一起
        CollUtil.addAll(permissionSet, roleSet);
        CollUtil.addAll(permissionSet, moduleSet);
        return ResponseData.success(permissionSet);
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfo", name = "sysUserInfo", value = "对象参数", required = true)
    })
    @PostMapping(value = "/resetPassword")
    public ResponseData resetPassword(@RequestBody SysUserInfo sysUserInfo) {
        sysUserInfo.setPassword("{bcrypt}" + passwordEncoder.encode(sysUserInfo.getPassword()));
        return ResponseData.success(baseService.updateById(sysUserInfo));
    }

    @ApiOperation(value = "获取已授权的角色列表", notes = "根据用户id查询已授权的角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAuthRoleListByUserId")
    public ResponseData getAuthRoleListByUserId(String userId) {
        return ResponseData.success(baseService.getAuthRoleListByUserId(userId));
    }


    @ApiOperation(value = "保存授权角色", notes = "保存授权角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfoRoleDTO", name = "sysUserInfoRoleDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthRole")
    public ResponseData saveAuthRole(@RequestBody SysUserInfoRoleDTO sysUserInfoRoleDTO) {
        return ResponseData.success(baseService.saveAuthRole(sysUserInfoRoleDTO));
    }

    @ApiOperation(value = "获取已授权的用户组列表", notes = "根据用户id查询已授权的用户组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAuthGroupListByUserId")
    public ResponseData getAuthGroupListByUserId(String userId) {
        return ResponseData.success(baseService.getAuthGroupListByUserId(userId));
    }


    @ApiOperation(value = "保存授权用户组", notes = "保存授权用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfoGroupDTO", name = "sysUserInfoGroupDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthGroup")
    public ResponseData resetPassword(@RequestBody SysUserInfoGroupDTO sysUserInfoGroupDTO) {
        return ResponseData.success(baseService.saveAuthGroup(sysUserInfoGroupDTO));
    }
}
