package com.dcy.api;

import com.dcy.api.dto.SysUserGroupRoleDTO;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysUserGroupService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/group")
@Api(value = "SysUserGroupController", tags = {"用户组操作接口"})
public class SysUserGroupController extends BaseController<ISysUserGroupService, SysUserGroup> {


    @ApiOperation(value = "获取已授权的角色列表", notes = "根据用户组id查询已授权的角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGroupId", value = "用户组Id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAuthRoleListByUserGroupId")
    public ResponseData<List<SysRole>> getAuthRoleListByUserGroupId(String userGroupId) {
        return ResponseData.success(baseService.getAuthRoleListByUserGroupId(userGroupId));
    }


    @ApiOperation(value = "保存授权角色", notes = "保存授权角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserGroupRoleDTO", name = "sysUserGroupRoleDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthRole")
    public ResponseData<Boolean> saveAuthRole(@RequestBody SysUserGroupRoleDTO sysUserGroupRoleDTO) {
        return ResponseData.success(baseService.saveAuthRole(sysUserGroupRoleDTO));
    }
}
