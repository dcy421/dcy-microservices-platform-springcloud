package com.dcy.controller;

import com.dcy.api.model.SysOperation;
import com.dcy.service.ISysOperationService;
import com.dcy.web.base.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 功能操作表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/oper")
@Api(value = "SysOperationController", tags = {"功能操作接口"})
public class SysOperationController extends BaseController<ISysOperationService, SysOperation> {

}
