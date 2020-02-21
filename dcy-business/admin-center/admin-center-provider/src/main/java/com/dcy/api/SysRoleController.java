package com.dcy.api;

import com.dcy.api.dto.SysRolePowerDTO;
import com.dcy.api.model.SysPower;
import com.dcy.api.model.SysRole;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysRoleService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/role")
@Api(value = "SysRoleController", tags = {"角色操作接口"})
public class SysRoleController extends BaseController<ISysRoleService, SysRole> {

    @ApiOperation(value = "获取已授权的权限列表", notes = "根据角色id查询已授权的权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAuthPowerListByRoleId")
    public ResponseData<List<SysPower>> getAuthPowerListByRoleId(String roleId) {
        return ResponseData.success(baseService.getAuthPowerListByRoleId(roleId));
    }


    @ApiOperation(value = "保存授权权限", notes = "保存授权权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfoRoleDTO", name = "sysUserInfoRoleDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthPower")
    public ResponseData<Boolean> saveAuthPower(@RequestBody SysRolePowerDTO sysRolePowerDTO) {
        return ResponseData.success(baseService.saveAuthPower(sysRolePowerDTO));
    }
}
