package com.dcy.api;

import com.dcy.common.model.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程任务相关接口封装
 *
 * @author: linjinp
 * @create: 2019-11-05 14:55
 **/
@RestController
@RequestMapping("/flowable/task/api")
@Api(value = "TaskApiController", tags = {"任务操作接口"})
public class TaskApiController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @ApiOperation(value = "根据任务id判断是否挂起", notes = "根据任务id判断是否挂起 true 挂起， false 未挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/taskIsSuspended/{taskId}")
    public ResponseData<Boolean> taskIsSuspended(@PathVariable(value = "taskId") String taskId) {
        Boolean isSuspended = taskService.createTaskQuery().taskId(taskId).singleResult().isSuspended();
        return ResponseData.success(isSuspended);
    }
}