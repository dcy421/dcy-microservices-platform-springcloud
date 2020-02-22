package com.dcy.api;

import com.dcy.common.model.ResponseData;
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
    private FlowableModelQueryService modelQueryService;

    @Autowired
    private ModelHistoryResource modelHistoryResource;

    @Autowired
    private ModelBpmnResource modelBpmnResource;

    @Autowired
    private ModelService modelService;

    @Autowired
    private RepositoryService repositoryService;


    @ApiOperation(value = "下载 Bpmn20.xml", notes = "下载 Bpmn20.xml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "model ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/getProcessModelBpmn20Xml/{modelId}")
    public void getProcessModelBpmn20Xml(HttpServletResponse response, @PathVariable(value = "modelId") String modelId) throws IOException {
        modelBpmnResource.getProcessModelBpmn20Xml(response, modelId);
    }

    /**
     * 流程部署
     *
     * @param modelId 流程ID，来自 act_de_model 的id
     * @return
     */
    @ApiOperation(value = "流程部署", notes = "流程部署")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "model ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/flowDeployment/{modelId}")
    public ResponseData<String> flowDeployment(@PathVariable(value = "modelId") String modelId) {
        try {
            // 根据模型 ID 获取模型
            Model modelData = modelService.getModel(modelId);

            byte[] bytes = modelService.getBpmnXML(modelData);
            if (bytes == null) {
                logger.error("模型数据为空，请先设计流程并成功保存，再进行发布");
                return ResponseData.error("模型数据为空，请先设计流程并成功保存，再进行发布");
            }

            BpmnModel model = modelService.getBpmnModel(modelData);
            if (model.getProcesses().size() == 0) {
                logger.error("数据模型不符要求，请至少设计一条主线流程");
                return ResponseData.error("数据模型不符要求，请至少设计一条主线流程");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String processName = modelData.getName() + ".bpmn20.xml";

            // 部署流程
            repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addBytes(processName, bpmnBytes)
                    .deploy();
        } catch (Exception exception) {
            // 发生异常，说明流程图配置存在问题，返回错误
            return ResponseData.error("发布失败，流程图不正确");
        }
        logger.info("流程部署成功：" + modelId + " " + new Date());
        return ResponseData.success("发布成功");
    }


    @ApiOperation(value = "根据流程定义id 操作挂起激活", notes = "根据流程定义id 操作挂起激活 true 挂起， false 未挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinedId", value = "流程定义id", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/processDefinedHangChange/{processDefinedId}")
    public ResponseData<String> processDefinedHangChange(@PathVariable(value = "processDefinedId") String processDefinedId) {
        // 判断挂起状态，true 挂起， false 未挂起
        if (repositoryService.isProcessDefinitionSuspended(processDefinedId)) {
            // 激活
            repositoryService.activateProcessDefinitionById(processDefinedId);
        } else {
            // 挂起
            repositoryService.suspendProcessDefinitionById(processDefinedId);
        }
        return ResponseData.success();
    }


    /**
     * 删除流程定义
     *
     * @param deploymentId 部署ID act_re_deployment 的id
     * @param isForce      是否强制删除
     * @return
     */
    @ApiOperation(value = "根据流程定义id 操作挂起激活", notes = "根据流程定义id 操作挂起激活 true 挂起， false 未挂起")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "流程定义id", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "isForce", value = "是否级联删除，0是，1否", dataType = "String", paramType = "path", required = true, defaultValue = "1"),
    })
    @DeleteMapping(value = "/deleteProcessDefined/{deploymentId}/{isForce}")
    public ResponseData<String> deleteProcessDefined(@PathVariable(value = "deploymentId") String deploymentId, @PathVariable(value = "isForce") String isForce) {
        if ("0".equals(isForce)) {
            // 级联删除，实例，历史都会被删除
            repositoryService.deleteDeployment(deploymentId, true);
        } else {
            try {
                // 普通删除
                repositoryService.deleteDeployment(deploymentId);
            } catch (PersistenceException e) {
                return ResponseData.error("有正在运行的流程，不能删除");
            }
        }
        return ResponseData.success();
    }


    @ApiOperation(value = "获取全部模型", notes = "获取全部模型")
    @GetMapping(value = "/flowModels")
    public ResponseData<List<ModelRepresentationVo>> flowModels(HttpServletRequest request) {
        List<ModelRepresentation> modelList = (List<ModelRepresentation>) modelQueryService.getModels("processes", "modifiedDesc", 0, request).getData();
        List<ModelRepresentationVo> modelsVo = new ArrayList<>();
        modelList.forEach(model -> modelsVo.add(new ModelRepresentationVo(model)));
        // 获取已经发布的流程信息
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        modelsVo.forEach(modelVo -> processDefinitionList.forEach(processDefinition -> {
            // 匹配确认模型是否已经发布过
            if (modelVo.getKey().equals(processDefinition.getKey())) {
                modelVo.setIsDeployment(true);
            }
        }));
        return ResponseData.success(modelsVo);
    }


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