package com.dcy.api;

import com.dcy.api.model.SysModuleResources;
import com.dcy.api.service.SysModuleResourcesRemoteService;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysModuleResourcesService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/module")
@Api(value = "SysModuleResourcesController", tags = {"模块操作接口"})
public class SysModuleResourcesController extends BaseController<ISysModuleResourcesService, SysModuleResources> implements SysModuleResourcesRemoteService {

    @ApiOperation(value = "获取tree-table列表数据", notes = "获取tree-table列表数据")
    @GetMapping(value = "/getModuleTreeTableList")
    public ResponseData<List<SysModuleResources>> getModuleTreeTableList() {
        return ResponseData.success(baseService.getModuleTreeTableList());
    }

    @ApiOperation(value = "获取tree列表数据", notes = "获取tree列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "powerId", paramType = "query")
    })
    @GetMapping(value = "/getModuleTreeListByPowerId")
    public ResponseData<List<String>> getModuleTreeListByPowerId(String powerId) {
        return ResponseData.success(baseService.getModuleTreeListByPowerId(powerId));
    }

    @ApiOperation(value = "根据用户id查询对应资源", notes = "根据用户id查询对应资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getModuleByUserId")
    @Override
    public ResponseData<List<Map<String, Object>>> getModuleByUserId(@RequestParam(value = "userId") String userId) {
        return ResponseData.success(baseService.getModuleByUserId(userId));
    }
}
