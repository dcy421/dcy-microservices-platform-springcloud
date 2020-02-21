package com.dcy.api;

import com.dcy.utils.FlowableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * @Author：dcy
 * @Description: 流程图相关接口封装
 * @Date: 2020-02-21 10:17
 */
@RestController
@RequestMapping("/flowable/diagram/api")
public class DiagramApiController {

    public static final Logger logger = LogManager.getLogger(DiagramApiController.class);

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 获取流程图
     * @param processDefinedId
     * @param httpServletResponse
     */
    @GetMapping(value = "/getFlowDiagram/{processDefinedId}")
    public void getFlowDiagram(@PathVariable(value = "processDefinedId") String processDefinedId, HttpServletResponse httpServletResponse) throws IOException {
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinedId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "bmp", new ArrayList<>(), flows, processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), 1.0, true);

        byte[] buf = new byte[1024];
        int length;
        OutputStream out = httpServletResponse.getOutputStream();
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        in.close();
        out.close();
    }

    /**
     * 流程实例，显示流程进度图
     * @param processInstanceId
     * @param httpServletResponse
     */
    @GetMapping(value = "/getProcessInstanceDiagram/{processInstanceId}")
    public void getProcessInstanceDiagram(@PathVariable(value = "processInstanceId") String processInstanceId, HttpServletResponse httpServletResponse) throws IOException {
        String processDefinedId;
        // 查看完成的进程中是否存在此进程
        long count = historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count();
        if (count > 0) {
            // 如果流程已经结束，则得到结束节点
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            processDefinedId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinedId = pi.getProcessDefinitionId();
        }

        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinedId).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);

        // 获取全部历史节点活动实例，即已经走过的节点历史
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        // 循环放入栈，栈 LIFO：后进先出
        Stack<HistoricTaskInstance> stack = new Stack<>();
        historicTaskInstanceList.forEach(item -> stack.push(item));
        // 历史任务实例清洗
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        List<String> highLightedActivitis = new ArrayList<>();
        lastHistoricTaskInstanceList.forEach(item -> highLightedActivitis.add(item));


        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinedId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivitis, flows, processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), 1.0, true);

        byte[] buf = new byte[1024];
        int length;
        OutputStream out = httpServletResponse.getOutputStream();
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        in.close();
        out.close();
    }


    /**
     * 流程任务，显示流程进度图
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getTaskDiagram/{taskId}")
    public void getTaskDiagram(@PathVariable(value = "taskId") String taskId, HttpServletResponse httpServletResponse) throws IOException {

        // 根据任务 ID 获取流程实例 ID
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String processDefinedId;

        // 查看完成的进程中是否存在此进程
        long count = historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count();
        if (count > 0) {
            // 如果流程已经结束，则得到结束节点
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            processDefinedId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinedId = pi.getProcessDefinitionId();
        }
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinedId).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);

        // 获取全部历史节点活动实例，即已经走过的节点历史
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        // 循环放入栈，栈 LIFO：后进先出
        Stack<HistoricTaskInstance> stack = new Stack<>();
        historicTaskInstanceList.forEach(item -> stack.push(item));
        // 历史任务实例清洗
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        List<String> highLightedActivitis = new ArrayList<>();
        lastHistoricTaskInstanceList.forEach(item -> highLightedActivitis.add(item));

        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinedId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivitis, flows, processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), 1.0, true);

        byte[] buf = new byte[1024];
        int length;
        OutputStream out = httpServletResponse.getOutputStream();
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        in.close();
        out.close();
    }

}

