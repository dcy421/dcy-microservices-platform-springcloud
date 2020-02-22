package com.dcy.api;

import com.dcy.common.model.ResponseData;
import com.dcy.entity.ProcessInstanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义与实例相关接口封装
 *
 * @author: linjinp
 * @create: 2019-11-05 14:55
 **/
@RestController
@RequestMapping("/flowable/runtime/api")
@Api(value = "HistoryApiController", tags = {"流程运行操作接口"})
public class RuntimeApiController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    @ApiOperation(value = "根据流程实例id 操作挂起激活", notes = "true 挂起， false 未挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/processInstanceHangChange/{processInstanceId}")
    public ResponseData<String> processInstanceHangChange(@PathVariable(value = "processInstanceId") String processInstanceId) {
        // 判断挂起状态，true 挂起， false 未挂起
        if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended()) {
            // 激活
            runtimeService.activateProcessInstanceById(processInstanceId);
        } else {
            // 挂起
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }
        return ResponseData.success();
    }

    /**
     * 流程实例列表查询
     *
     * @param flowableQueryEntity
     * @return
     */
    /*@PostMapping(value = "/getFlowinfo")
    public ResponseData<String> getFlowinfo(@RequestBody FlowableQueryEntity flowableQueryEntity) {
        // 页码
        Integer page = flowableQueryEntity.getPage();
        // 条数
        Integer limit = flowableQueryEntity.getLimit();
        // 租户列表
        List<String> tenantList = flowableQueryEntity.getTenantList();
        // 关键字
        String keyword = flowableQueryEntity.getKeyword();

        // 把租户列表转化为逗号分隔的字符串
        String tenantIds = "\"" + Joiner.on("\",\"").join(tenantList) + "\"";
        //
        Integer startIndex = PageEntity.startIndex(page, limit);
        String sql = "SELECT * FROM " + managementService.getTableName(ProcessInstance.class) +
                " WHERE ID_ = PROC_INST_ID_ " +
                "AND TENANT_ID_ IN ("+ tenantIds +") ";
        if (keyword != null && !"".equals(keyword)) {
            sql = sql + "AND (ID_ LIKE '%"+ keyword +"%' OR NAME_ LIKE '%"+ keyword +"%' OR PROC_DEF_ID_ LIKE '%"+ keyword +"%' OR START_USER_ID_ LIKE '%"+ keyword +"%')";
        }
        sql = sql + "ORDER BY START_TIME_ DESC ";
        // 获取数据总条数
        Integer total = runtimeService.createNativeProcessInstanceQuery().sql(sql).list().size();
        // 拼接分页
        sql = sql + "LIMIT "+ startIndex +","+ limit;
        // 获取当前页数据
        List<ProcessInstance> processInstanceList = runtimeService.createNativeProcessInstanceQuery().sql(sql).list();
        List<ProcessInstanceVo> list = new ArrayList<>();
        processInstanceList.forEach(processInstance -> list.add(new ProcessInstanceVo(processInstance)));
        // 分页数据组装
        PageEntity pageEntity = new PageEntity(page, limit, total);
        pageEntity.setData(list);
        return JsonUtil.toJSON(ErrorMsg.SUCCESS.setNewData(pageEntity));
    }*/
    @PostMapping(value = "/getFlowinfo")
    public ResponseData<List<ProcessInstanceVo>> getFlowinfo() {
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().list();
        List<ProcessInstanceVo> list = new ArrayList<>();
        processInstanceList.forEach(processInstance -> list.add(new ProcessInstanceVo(processInstance)));
        return ResponseData.success(list);
    }




    @ApiOperation(value = "根据流程实例id判断是否挂起", notes = "根据流程实例id判断是否挂起 true 挂起， false 未挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/processIsSuspended/{processInstanceId}")
    public ResponseData<Boolean> processIsSuspended(@PathVariable(value = "processInstanceId") String processInstanceId) {
        Boolean isSuspended = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended();
        return ResponseData.success(isSuspended);
    }

}