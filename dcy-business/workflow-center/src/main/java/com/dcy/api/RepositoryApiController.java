package com.dcy.api;

import cn.hutool.core.util.PageUtil;
import com.dcy.common.model.ResponseData;
import com.dcy.db.base.model.PageModel;
import com.dcy.entity.ModelRepresentationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.rest.app.ModelBpmnResource;
import org.flowable.ui.modeler.rest.app.ModelHistoryResource;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 流程部署及定义相关接口封装
 *
 * @author: linjinp
 * @create: 2019-11-05 14:55
 **/
@RestController
@RequestMapping("/repository")
@Api(value = "RepositoryApiController", tags = {"部署及定义操作接口"})
public class RepositoryApiController {

    public static final Logger logger = LogManager.getLogger(RepositoryApiController.class);

    @Autowired
    private RepositoryService repositoryService;



    /**
     * 流程模型数据
     *
     * @return
     */
    @ApiOperation(value = "根据流程定义id 获取流程模型数据", notes = "根据流程定义id 获取流程模型数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinedId", value = "流程定义id", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/getFlowModel/{processDefinedId}")
    public ResponseData<List<Process>> getFlowModel(@PathVariable(value = "processDefinedId") String processDefinedId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinedId);
        return ResponseData.success(bpmnModel.getProcesses());
    }


    @ApiOperation(value = "获取流程定义所有用户任务节点列表", notes = "获取流程定义所有用户任务节点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinedKey", value = "流程定义key", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/getProcessDefinedTaskList/{processDefinedKey}")
    public ResponseData<List<Map<String, Object>>> getProcessDefinedTaskList(@PathVariable(value = "processDefinedKey") String processDefinedKey) {
        List<Map<String, Object>> list = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinedKey).latestVersion().singleResult();
        //获取所有节点信息
        List<Process> processes = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses();
        for (Process process : processes) {
            Collection<FlowElement> flowElements = process.getFlowElements();
            if (flowElements != null) {
                for (FlowElement flowElement : flowElements) {
                    // 类型为用户节点
                    if (flowElement instanceof UserTask) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("key", flowElement.getId());
                        map.put("name", flowElement.getName());
                        list.add(map);
                    }
//                    // 类型为开始节点
//                    if (flowElement instanceof StartEvent) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("key", flowElement.getId());
//                        map.put("name", flowElement.getName());
//                        list.add(map);
//                    }
//                    // 类型为结束节点
//                    if (flowElement instanceof EndEvent) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("key", flowElement.getId());
//                        map.put("name", flowElement.getName());
//                        list.add(map);
//                    }
                }
            }
        }
        return ResponseData.success(list);
    }


}