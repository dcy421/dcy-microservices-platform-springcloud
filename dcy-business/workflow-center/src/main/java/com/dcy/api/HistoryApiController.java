package com.dcy.api;

import com.alibaba.fastjson.JSON;
import com.dcy.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-21 10:23
 */
@RestController
@RequestMapping("/flowable/history/api")
public class HistoryApiController {

    public static final Logger logger = LogManager.getLogger(HistoryApiController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 流程实例历史
     *
     * @return
     */
    @GetMapping(value = "/getFlowInfoHistory")
    public String getFlowInfoHistory() {
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime().desc().list();
        List<HistoricProcessInstanceVo> list = new ArrayList<>();
        historicProcessInstanceList.forEach(historicProcessInstance -> list.add(new HistoricProcessInstanceVo(historicProcessInstance)));
        return JSON.toJSONString(list);
    }

    /**
     * 根据流程实例ID，对流程任务历史进行查询
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/getHistoricProcessInstanceTask/{processInstanceId}")
    public String getHistoricProcessInstanceTask(@PathVariable(value = "processInstanceId") String processInstanceId) {
        // 获取活动节点，用户任务节点部分
        List<HistoricActivityInstance> historicActivityList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityType("userTask").orderByHistoricActivityInstanceStartTime().asc().list();
        List<HistoricActivityInstanceVo> list = new ArrayList<>();
        historicActivityList.forEach(item -> {
            HistoricActivityInstanceVo historicTaskInstanceVo = new HistoricActivityInstanceVo(item);
            List<CommentVo> commentVoList = new ArrayList<>();
            // 获取并设置批注，即审核原因，驳回原因之类的
            List<Comment> commentList = taskService.getTaskComments(item.getTaskId());
            // 注意，批注的顺序为时间倒序，因此按倒序取出
            for (int i = commentList.size() - 1; i >= 0; i--) {
                commentVoList.add(new CommentVo(commentList.get(i)));
            }
            historicTaskInstanceVo.setComment(commentVoList);
            list.add(historicTaskInstanceVo);
        });
        return JSON.toJSONString(list);
    }

    /**
     * 根据任务ID，对流程任务历史进行查询
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getHistoricFlowTask/{taskId}")
    public String getHistoricFlowTask(@PathVariable(value = "taskId") String taskId) {
        // 获取任务节点信息
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        // 获取活动节点，用户任务节点部分
        List<HistoricActivityInstance> historicActivityList = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).activityType("userTask").orderByHistoricActivityInstanceStartTime().asc().list();
        List<HistoricActivityInstanceVo> list = new ArrayList<>();
        historicActivityList.forEach(item -> {
            HistoricActivityInstanceVo historicTaskInstanceVo = new HistoricActivityInstanceVo(item);
            List<CommentVo> commentVoList = new ArrayList<>();
            // 获取并设置批注，即审核原因，驳回原因之类的
            List<Comment> commentList = taskService.getTaskComments(item.getTaskId());
            // 注意，批注的顺序为时间倒序，因此按倒序取出
            for (int i = commentList.size() - 1; i >= 0; i--) {
                commentVoList.add(new CommentVo(commentList.get(i)));
            }
            historicTaskInstanceVo.setComment(commentVoList);
            list.add(historicTaskInstanceVo);
        });
        return JSON.toJSONString(list);
    }

    /**
     * 获取用户流程发起历史
     *
     * @param flowableQueryEntity
     * @return
     */
    @PostMapping(value = "/getAuthenticatedUserProcessInstanceHistory")
    public String getAuthenticatedUserProcessInstanceHistory(@RequestBody FlowableQueryEntity flowableQueryEntity) {
        // 审核人
        String assignee = flowableQueryEntity.getAssignee();
        // 租户
        String tanantId = flowableQueryEntity.getTenantId();
        // 开始时间
        Date startTime = flowableQueryEntity.getStartTime();
        // 结束时间
        Date endTime = flowableQueryEntity.getEndTime();
        // 页码
        Integer page = flowableQueryEntity.getPage();
        // 条数
        Integer limit = flowableQueryEntity.getLimit();
        // 获取发起人为当前用户的流程实例
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().startedBy(assignee);
        if (startTime != null) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.startedAfter(startTime);
        }
        if (endTime != null) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.startedBefore(endTime);
        }
        if (tanantId != null && !"".equals(tanantId)) {
            historicProcessInstanceQuery = historicProcessInstanceQuery.processInstanceTenantId(tanantId);
        }
        // 总条数
        Long total = historicProcessInstanceQuery.count();
        // 查询数据列表
//        List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery.orderBy(HistoricProcessInstanceQueryProperty.END_TIME, Query.NullHandlingOnOrder.NULLS_FIRST).asc().orderByProcessInstanceStartTime().desc().listPage(PageEntity.startIndex(page, limit), limit);
        List<HistoricProcessInstanceVo> list = new ArrayList<>();
        /*if (historicProcessInstanceList != null) {
            historicProcessInstanceList.forEach(item -> list.add(new HistoricProcessInstanceVo(item)));
        }*/
        return JSON.toJSONString(list);
    }

    /**
     * 获取历史任务记录，分页
     *
     * @param flowableQueryEntity
     * @return
     */
    @PostMapping(value = "/getTaskHistory")
    public String getTaskHistory(@RequestBody FlowableQueryEntity flowableQueryEntity) {
        // 审核人
        String assignee = flowableQueryEntity.getAssignee();
        // 租户
        String tanantId = flowableQueryEntity.getTenantId();
        // 开始时间
        Date startTime = flowableQueryEntity.getStartTime();
        // 结束时间
        Date endTime = flowableQueryEntity.getEndTime();
        // 页码
        Integer page = flowableQueryEntity.getPage();
        // 条数
        Integer limit = flowableQueryEntity.getLimit();
        // 获取历史任务记录
        HistoricTaskInstanceQuery historyTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(assignee);
        if (startTime != null) {
            historyTaskQuery = historyTaskQuery.taskCreatedAfter(startTime);
        }
        if (endTime != null) {
            historyTaskQuery = historyTaskQuery.taskCreatedBefore(endTime);
        }
        if (tanantId != null && !"".equals(tanantId)) {
            historyTaskQuery = historyTaskQuery.taskTenantId(tanantId);
        }
        // 总条数
        Long total = historyTaskQuery.count();
        // 查询数据列表
//        List<HistoricTaskInstance> historicTaskInstanceList = historyTaskQuery.orderBy(HistoricTaskInstanceQueryProperty.END, Query.NullHandlingOnOrder.NULLS_FIRST).orderByHistoricTaskInstanceEndTime().desc().listPage(PageEntity.startIndex(page, limit), limit);
        List<HistoricTaskInstanceVo> list = new ArrayList<>();
        /*if (historicTaskInstanceList != null) {
            historicTaskInstanceList.forEach(item -> {
                HistoricTaskInstanceVo historicTaskInstanceVo = new HistoricTaskInstanceVo(item);
                // 获取任务的历史流程实例数据
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(item.getProcessInstanceId()).singleResult();
                historicTaskInstanceVo.setHistoricProcessInstance(new HistoricProcessInstanceVo(historicProcessInstance));
                list.add(historicTaskInstanceVo);
            });
        }*/
        /*PageEntity pageEntity = new PageEntity(page, limit, total);
        pageEntity.setData(list);*/
        return JSON.toJSONString(list);
    }
}

