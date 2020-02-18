package com.dcy.controller;

import com.dcy.api.model.SysConfig;
import com.dcy.common.model.ResponseData;
import com.dcy.service.ISysConfigService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/config")
@Api(value = "SysConfigController", tags = {"参数配置操作接口"})
public class SysConfigController extends BaseController<ISysConfigService, SysConfig> {


    @ApiOperation(value = "根据配置key查询配置value", notes = "根据配置key查询配置value")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "参数键名", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getValueByKey")
    public ResponseData<String> getValueByKey(String key) {
        return ResponseData.success(baseService.getValueByKey(key));
    }

}
