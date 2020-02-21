package com.dcy.api;

import com.dcy.api.dto.SysPowerMenuDTO;
import com.dcy.api.dto.SysPowerModuleDTO;
import com.dcy.api.model.SysPower;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysPowerService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/power")
@Api(value = "SysPowerController", tags = {"权限操作接口"})
public class SysPowerController extends BaseController<ISysPowerService, SysPower> {

    @ApiOperation(value = "保存授权模块", notes = "保存授权模块")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysPowerModuleDTO", name = "sysPowerModuleDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthModule")
    public ResponseData<Boolean> saveAuthModule(@RequestBody SysPowerModuleDTO sysPowerModuleDTO) {
        return ResponseData.success(baseService.saveAuthModule(sysPowerModuleDTO));
    }

    @ApiOperation(value = "保存菜单模块", notes = "保存菜单模块")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysPowerMenuDTO", name = "sysPowerMenuDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthMenu")
    public ResponseData<Boolean> saveAuthMenu(@RequestBody SysPowerMenuDTO sysPowerMenuDTO) {
        return ResponseData.success(baseService.saveAuthMenu(sysPowerMenuDTO));
    }
}
