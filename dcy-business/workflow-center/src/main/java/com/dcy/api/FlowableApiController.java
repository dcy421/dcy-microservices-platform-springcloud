package com.dcy.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.model.ResponseData;
import com.dcy.dto.ProcessInstanceDTO;
import com.dcy.dto.TaskDTO;
import com.dcy.entity.ProcessInstanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-21 10:22
 */
@Slf4j
@RestController
@RequestMapping("/flowable/api")
@Api(value = "FlowableApiController", tags = {"流程api操作接口"})
public class FlowableApiController {

    // 用户以及组管理服务
    @Autowired
    private IdentityService identityService;

    // 流程实例服务
    @Autowired
    private RuntimeService runtimeService;

    // 流程节点任务服务
    @Autowired
    private TaskService taskService;

    // 历史数据服务
    @Autowired
    private HistoryService historyService;

    @ApiOperation(value = "启动流程", notes = "启动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceDTO", value = "流程id", dataType = "ProcessInstanceDTO", paramType = "body", required = true)
    })
    @PostMapping("/start")
    public ResponseData<ProcessInstanceVo> start(@RequestBody ProcessInstanceDTO processInstanceDTO) {
        // 设置发起人
        identityService.setAuthenticatedUserId(processInstanceDTO.getUserId());
        Map<String, Object> variables = processInstanceDTO.getVariables();
        if (CollUtil.isEmpty(variables)){
            variables = CollUtil.newHashMap();
            variables.put("businessKey",processInstanceDTO.getBusinessKey());
        }else{
            variables.put("businessKey",processInstanceDTO.getBusinessKey());
        }
        // 根据流程 ID 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceDTO.getProcessDefinitionKey(), processInstanceDTO.getBusinessKey(), variables);
        log.info("流程启动成功：" + processInstance.getId() + " " + new Date());
        return ResponseData.success(new ProcessInstanceVo(processInstance));
    }

    @ApiOperation(value = "签收任务", notes = "签收任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "path", required = true),
    })
    @PostMapping("/claim/{taskId}/{userId}")
    public ResponseData<String> claim(@PathVariable(value = "taskId") String taskId, @PathVariable(value = "userId") String userId) {
        // 签收任务
        taskService.claim(taskId, userId);
        return ResponseData.success();
    }

    @ApiOperation(value = "完成任务", notes = "完成任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskDTO", value = "任务对象", dataType = "TaskDTO", paramType = "body", required = true)
    })
    @PostMapping("/complete")
    public ResponseData<String> task(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.createTaskQuery().taskId(taskDTO.getTaskId()).singleResult();
        if (task.isSuspended()) {
            return ResponseData.error("任务已挂起，无法完成");
        }
        if (StrUtil.isBlank(task.getAssignee()) && StrUtil.isBlank(taskDTO.getUserId())) {
            return ResponseData.error("请设置完成人");
        }
        if (StrUtil.isNotBlank(taskDTO.getUserId())) {
            // 设置完成人
            taskService.setAssignee(taskDTO.getTaskId(), taskDTO.getUserId());
        }
        if (StrUtil.isNotBlank(taskDTO.getProcessInstanceId()) && StrUtil.isNotBlank(taskDTO.getComment())) {
            // 保存意见
            taskService.addComment(taskDTO.getTaskId(), taskDTO.getProcessInstanceId(), "taskStatus", taskDTO.getAdopt() ? "success" : "reject");
            taskService.addComment(taskDTO.getTaskId(), taskDTO.getProcessInstanceId(), "taskMessage", taskDTO.getAdopt() ? "已通过" : "已驳回");
            taskService.addComment(taskDTO.getTaskId(), taskDTO.getProcessInstanceId(), "taskComment", taskDTO.getComment());
        }
        // 完成任务
        taskService.complete(taskDTO.getTaskId(), taskDTO.getVariables());
        return ResponseData.success();
    }


    @ApiOperation(value = "根据用户获取代表列表流程实例ids", notes = "根据用户获取代表列表流程实例ids")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/getRunProInsIdList/{userId}")
    public ResponseData<List<String>> getRunList(@PathVariable(value = "userId") String userId) {
        // =============== 已签收和未签收同时查询 ===============
        List<String> result = taskService.createTaskQuery().taskCandidateOrAssigned(userId).active().list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());
        return ResponseData.success(result);
    }


    @ApiOperation(value = "更具用户Id获取历史任务流程实例ids", notes = "更具用户Id获取历史任务流程实例ids")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/getHisProInsIdList/{userId}")
    public ResponseData<List<String>> getHisList(@PathVariable(value = "userId") String userId) {
        List<String> result = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
                .orderByHistoricTaskInstanceEndTime().desc().list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());
        return ResponseData.success(result);
    }


    @ApiOperation(value = "根据流程实例id删除流程实例", notes = "根据流程实例id删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例id", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/deleteProcess/{processId}")
    public void deleteProcess(@PathVariable(value = "processId") String processId) {
        runtimeService.deleteProcessInstance(processId, "删除流程");
    }


    @ApiOperation(value = "获取正在运行的数据 Id 列表", notes = "获取正在运行的数据 Id 列表")
    @GetMapping("/getRuntimeDataId")
    public List<String> getRuntimeDataId() {
        List<String> idList = new ArrayList<>();
        // 获取正在执行的任务列表
        List<Execution> list = runtimeService.createExecutionQuery().onlyProcessInstanceExecutions().list();
        list.forEach(execution -> {
            // 根据任务获取流程实例
            // 获取流程实例种的业务 key
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
            idList.add(pi.getBusinessKey());
        });
        return idList;
    }


    @ApiOperation(value = "根据用户，获取需要审核的业务键 business_key 列表", notes = "根据用户，获取需要审核的业务键 business_key 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "流程实例id", dataType = "String", paramType = "path", required = true)
    })
    @RequestMapping(value = "/getRuntimeBusinessKeyByUser/{userId}", method = RequestMethod.GET)
    public List<Map<String, Object>> getRuntimeBusinessKeyByUser(@PathVariable(value = "userId") String userId) {
        List<Map<String, Object>> idList = new ArrayList<>();
        // 根据用户获取正在进行的任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
        tasks.forEach(task -> {
            Map<String, Object> data = new HashMap<>();
            // 根据任务获取流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            // 获取流程实例中的业务键
            data.put("businessKey", processInstance.getBusinessKey());
            // 获取任务 Id
            data.put("taskId", task.getId());
            // 流程定义名称
            data.put("processInstanceName", processInstance.getProcessDefinitionName());
            // 流程开始时间
            data.put("startTime", processInstance.getStartTime());
            idList.add(data);
        });
        return idList;
    }


    @ApiOperation(value = "根据流程实例id判断流程是否结束", notes = "根据流程实例id判断流程是否结束,true 结束，false 未结束")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", dataType = "String", paramType = "path", required = true)
    })
    @RequestMapping(value = "/checkProcessInstanceFinish/{processInstanceId}", method = RequestMethod.GET)
    public ResponseData<Boolean> checkProcessInstanceFinish(@PathVariable(value = "processInstanceId") String processInstanceId) {
        boolean isFinish = false;
        // 根据流程 ID 获取未完成的流程中是否存在此流程
        long count = historyService.createHistoricProcessInstanceQuery().unfinished().processInstanceId(processInstanceId).count();
        // 不存在说明没有结束
        if (count == 0) {
            isFinish = true;
        }
        return ResponseData.success(isFinish);
    }


}
