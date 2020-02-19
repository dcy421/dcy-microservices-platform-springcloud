package com.dcy.controller;

import com.dcy.api.dto.TreeData;
import com.dcy.api.model.SysMenu;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysMenuService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/menu")
@Api(value = "SysMenuController", tags = {"菜单操作接口"})
public class SysMenuController extends BaseController<ISysMenuService, SysMenu> {

    @ApiOperation(value = "获取tree-table列表数据", notes = "获取tree-table列表数据")
    @GetMapping(value = "/getMenuTreeTableList")
    public ResponseData<List<SysMenu>> getMenuTreeTableList() {
        return ResponseData.success(baseService.getMenuTreeTableList());
    }


    @ApiOperation(value = "获取tree列表数据", notes = "获取tree列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "powerId", paramType = "query")
    })
    @GetMapping(value = "/getMenuTreeListByPowerId")
    public ResponseData<List<String>> getMenuTreeListByPowerId(String powerId) {
        return ResponseData.success(baseService.getMenuTreeListByPowerId(powerId));
    }
}
