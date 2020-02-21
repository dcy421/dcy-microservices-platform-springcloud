package com.dcy.api;

import com.dcy.common.model.ResponseData;
import com.dcy.entity.ProcessInstanceVo;
import com.dcy.utils.FlowableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 流程定义与实例相关接口封装
 *
 * @author: linjinp
 * @create: 2019-11-05 14:55
 **/
@RestController
@RequestMapping("/flowable/runtime/api")
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

    /**
     * 启动流程
     *
     * @param processDefinedId 流程定义 Id，来自 ACT_RE_PROCDEF
     * @param startUser        用户 Id
     * @param dataKey          数据 Key，业务键，一般为表单数据的 ID，仅作为表单数据与流程实例关联的依据 businessTable + ":" + businessId
     * @return
     */
    @GetMapping(value = "/flowStart/{processDefinedId}/{startUser}/{dataKey}")
    public ResponseData<String> flowStart(@PathVariable(value = "processDefinedId") String processDefinedId, @PathVariable(value = "startUser") String startUser, @PathVariable(value = "dataKey") String dataKey) {
        // 判断挂起状态，true 挂起， false 未挂起
        if (repositoryService.isProcessDefinitionSuspended(processDefinedId)) {
            return ResponseData.error("流程处于挂起状态，无法启动");
        }
        // 设置发起人
        identityService.setAuthenticatedUserId(startUser);
        // 预设该流程的流程定义 Key
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinedId).singleResult();
        Map<String, Object> map = new HashMap<>();
        map.put("processDefKey", pd.getKey());
        // 根据流程 ID 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinedId, dataKey, map);
        return ResponseData.success(processInstance.getId());
    }

    /**
     * 流程收回/驳回
     *
     * @param taskId  当前任务ID
     * @param comment 审核意见
     * @return
     */
    @GetMapping(value = "/flowTackback/{taskId}")
    public ResponseData<String> flowTackback(@PathVariable(value = "taskId") String taskId, @RequestParam(value = "comment", defaultValue = "") String comment) {
        if (taskService.createTaskQuery().taskId(taskId).singleResult().isSuspended()) {
            return ResponseData.error("任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }


        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            return ResponseData.error("当前节点为初始任务节点，不能驳回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }


        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));


        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            return ResponseData.error("任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置驳回信息
        currentTaskIds.forEach(item -> {
            taskService.addComment(item, null, "reject");
            taskService.addComment(item, null, "已驳回");
            taskService.addComment(item, null, comment);
        });

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            return ResponseData.error("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            return ResponseData.error("无法取消或开始活动");
        }
        return ResponseData.success();
    }

    /**
     * 流程回退
     *
     * @param taskId    当前任务ID
     * @param targetKey 要回退的任务 Key
     * @return
     */
    @GetMapping(value = "/flowReturn/{taskId}/{targetKey}")
    public ResponseData<String> flowReturn(@PathVariable(value = "taskId") String taskId, @PathVariable(value = "targetKey") String targetKey) {
        if (taskService.createTaskQuery().taskId(taskId).singleResult().isSuspended()) {
            return ResponseData.error("任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(targetKey)) {
                    target = flowElement;
                }
            }
        }


        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, targetKey, null, null);
        if (!isSequential) {
            return ResponseData.error("当前节点相对于目标节点，不属于串行关系，无法回退");
        }


        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退信息
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, null, "return");
            taskService.addComment(currentTaskId, null, "已退回");
            taskService.addComment(currentTaskId, null, "流程回退到" + target.getName() + "节点");
        }

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetKey).changeState();
        } catch (FlowableObjectNotFoundException e) {
            return ResponseData.error("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            return ResponseData.error("无法取消或开始活动");
        }
        return ResponseData.success();
    }

    /**
     * 流程实例挂起/激活挂起/激活
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
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
     * 加签
     *
     * @param parentExecutionId 父级活动 ID
     * @param processInstanceId
     * @param assigneeList
     * @return
     */
    @PostMapping(value = "/addExecution")
    public ResponseData<String> addExecution(@RequestParam(value = "parentExecutionId") String parentExecutionId, @RequestParam(value = "processInstanceId") String processInstanceId, @RequestBody List<String> assigneeList) {
        if (runtimeService.createExecutionQuery().executionId(parentExecutionId).singleResult().isSuspended()) {
            return ResponseData.error("任务处于挂起状态");
        }
        runtimeService.addMultiInstanceExecution(parentExecutionId, processInstanceId, Collections.singletonMap("addMultiAssigneeList", assigneeList));
        return ResponseData.success();
    }

    /**
     * 减签
     *
     * @param executionId 活动 ID
     * @param completed   是否完成此流程执行实例
     */
    @GetMapping(value = "deleteExecution/{executionId}/{completed}")
    public ResponseData<String> deleteExecution(@PathVariable("executionId") String executionId, @PathVariable("completed") Boolean completed) {
        if (runtimeService.createExecutionQuery().executionId(executionId).singleResult().isSuspended()) {
            return ResponseData.error("任务处于挂起状态");
        }
        runtimeService.deleteMultiInstanceExecution(executionId, completed);
        return ResponseData.success();
    }

    /**
     * 设置流程参数 List
     *
     * @param processInstanceId
     * @param key
     * @param value
     */
    @GetMapping(value = "/setListProcessVariable/{processInstanceId}/{key}/{value}")
    public ResponseData<String> setListProcessVariable(@PathVariable(value = "processInstanceId") String processInstanceId, @PathVariable(value = "key") String key, @PathVariable(value = "value") List value) {
        if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended()) {
            return ResponseData.error("任务处于挂起状态");
        }
        runtimeService.setVariable(processInstanceId, key, value);
        return ResponseData.success();
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    @DeleteMapping(value = "/deleteProcessInstance/{processInstanceId}")
    public ResponseData<String> deleteProcessInstance(@PathVariable(value = "processInstanceId") String processInstanceId) {
        runtimeService.deleteProcessInstance(processInstanceId, "中止流程");
        return ResponseData.success("流程已中止");
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

    /**
     * 执行任务是否挂起，true 挂起， false 未挂起
     *
     * @param executionId
     * @return
     */
    @GetMapping(value = "/executionIsSuspended/{executionId}")
    public ResponseData<Boolean> executionIsSuspended(@PathVariable(value = "executionId") String executionId) {
        Boolean isSuspended = runtimeService.createExecutionQuery().executionId(executionId).singleResult().isSuspended();
        return ResponseData.success(isSuspended);
    }


    /**
     * 流程实例是否挂起，true 挂起， false 未挂起
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/processIsSuspended/{processInstanceId}")
    public ResponseData<Boolean> processIsSuspended(@PathVariable(value = "processInstanceId") String processInstanceId) {
        Boolean isSuspended = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended();
        return ResponseData.success(isSuspended);
    }

    /**
     * 获取所有可回退的节点
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/findReturnUserTask/{taskId}")
    public ResponseData<List<UserTask>> findReturnUserTask(@PathVariable(value = "taskId") String taskId) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return ResponseData.success(userTaskList);
    }
}