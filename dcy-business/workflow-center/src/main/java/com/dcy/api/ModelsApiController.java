package com.dcy.api;

import com.dcy.common.model.ResponseData;
import com.dcy.entity.ModelRepresentationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.rest.app.ModelBpmnResource;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-23 11:00
 */
@Slf4j
@RestController
@RequestMapping("/models")
@Api(value = "ModelsApiController", tags = {"模型操作接口"})
public class ModelsApiController {

    @Autowired
    private FlowableModelQueryService modelQueryService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ModelBpmnResource modelBpmnResource;


    @ApiOperation(value = "获取全部模型", notes = "获取全部模型")
    @GetMapping(value = "/list")
    public ResponseData<List<ModelRepresentationVo>> list(HttpServletRequest request) {
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


    @ApiOperation(value = "流程部署", notes = "流程部署 来自 act_de_model 的id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "model ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/deploy/{modelId}")
    public ResponseData<String> deploy(@PathVariable(value = "modelId") String modelId) {
        try {
            // 根据模型 ID 获取模型
            Model modelData = modelService.getModel(modelId);

            byte[] bytes = modelService.getBpmnXML(modelData);
            if (bytes == null) {
                log.error("模型数据为空，请先设计流程并成功保存，再进行发布");
                return ResponseData.error("模型数据为空，请先设计流程并成功保存，再进行发布");
            }

            BpmnModel model = modelService.getBpmnModel(modelData);
            if (model.getProcesses().size() == 0) {
                log.error("数据模型不符要求，请至少设计一条主线流程");
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
        log.info("流程部署成功：" + modelId + " " + new Date());
        return ResponseData.success("发布成功");
    }


    @ApiOperation(value = "删除模型", notes = "删除模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模型id", dataType = "String", paramType = "path", required = true),
    })
    @DeleteMapping(value = "/{modelId}")
    public ResponseData<String> deleteProcessDefined(@PathVariable(value = "modelId") String modelId) {
        Model model = modelService.getModel(modelId);
        if (model != null) {
            modelService.deleteModel(model.getId());
        } else {
            return ResponseData.error("未找到模型");
        }
        return ResponseData.success();
    }


    @ApiOperation(value = "下载 Bpmn20.xml", notes = "下载 Bpmn20.xml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "model ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping(value = "/getProcessModelBpmn20Xml/{modelId}")
    public void getProcessModelBpmn20Xml(HttpServletResponse response, @PathVariable(value = "modelId") String modelId) throws IOException {
        modelBpmnResource.getProcessModelBpmn20Xml(response, modelId);
    }
}
