package com.dcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.model.SysDict;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysDictService;
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
 * 字典类型表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/dict")
@Api(value = "SysDictController", tags = {"字典操作接口"})
public class SysDictController extends BaseController<ISysDictService, SysDict> {

    @ApiOperation(value = "获取tree-table列表数据", notes = "获取tree-table列表数据")
    @GetMapping(value = "/getDictTreeTableList")
    public ResponseData<List<SysDict>> getDictTreeTableList() {
        return ResponseData.success(baseService.getDictTreeTableList());
    }

    @ApiOperation(value = "根据类型查询字典项", notes = "根据类型查询字典项（type字段）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "type字段", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getDictListByType")
    public ResponseData<List<SysDict>> getDictListByType(String type) {
        return ResponseData.success(baseService.list(new LambdaQueryWrapper<SysDict>().eq(SysDict::getType, type).orderByAsc(SysDict::getLocation)));
    }


    @ApiOperation(value = "根据分组类型查询字典项tree", notes = "根据分组类型查询字典项tree（dict_type字段）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupType", value = "dict_type字段", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getDictTreeListByGroupType")
    public ResponseData<List<SysDict>> getDictTreeListByGroupType(String groupType) {
        return ResponseData.success(baseService.getDictTreeListByGroupType(groupType));
    }
}
