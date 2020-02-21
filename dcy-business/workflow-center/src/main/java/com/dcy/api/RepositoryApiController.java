package com.dcy.api;

import com.dcy.common.model.ResponseData;
import com.dcy.entity.ModelRepresentationVo;
import com.dcy.entity.ProcessDefinitionVo;
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
import org.flowable.ui.common.model.BaseRestActionRepresentation;
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
@RequestMapping("/flowable/repository/api")
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

    /**
     * 流程模型版本回滚
     *
     * @param modelId        模型ID
     * @param modelHistoryId 历史版本ID
     * @return
     */
    @GetMapping(value = "/changeModelHistoryVersion/{modelId}/{version}/history/{modelHistoryId}/{historyVersion}")
    public ResponseData<String> changeModelHistoryVersion(@PathVariable(value = "modelId") String modelId, @PathVariable(value = "modelHistoryId") String modelHistoryId,
                                                          @PathVariable(value = "version") String version, @PathVariable(value = "historyVersion") String historyVersion) {
        BaseRestActionRepresentation baseRestActionRepresentation = new BaseRestActionRepresentation();
        baseRestActionRepresentation.setAction("useAsNewVersion");
        baseRestActionRepresentation.setComment("该版本为版本" + version + "回滚到版本" + historyVersion);
        try {
            modelHistoryResource.executeProcessModelHistoryAction(modelId, modelHistoryId, baseRestActionRepresentation);
        } catch (Exception e) {
            return ResponseData.error("版本回滚失败，请重试");
        }
        return ResponseData.success("版本回滚成功");
    }

    /**
     * 下载 Bpmn20.xml
     *
     * @param response
     * @param modelId
     * @return
     * @throws IOException
     */
    @PutMapping(value = "/getProcessModelBpmn20Xml/{modelId}")
    public void getProcessModelBpmn20Xml(HttpServletResponse response, @PathVariable(value = "modelId") String modelId) throws IOException {
        modelBpmnResource.getProcessModelBpmn20Xml(response, modelId);
    }

    /**
     * 流程部署
     *
     * @param modelId  流程ID，来自 ACT_DE_MODEL
     * @param tenantId 租户
     * @return
     */
    @GetMapping(value = "/flowDeployment/{modelId}/{tenantId}")
    public ResponseData<String> flowDeployment(@PathVariable(value = "modelId") String modelId, @PathVariable(value = "tenantId") String tenantId) {
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
                    .tenantId(tenantId)
                    .deploy();
        } catch (Exception exception) {
            // 发生异常，说明流程图配置存在问题，返回错误
            return ResponseData.error("发布失败，流程图不正确");
        }
        logger.info("流程部署成功：" + modelId + " " + new Date());
        return ResponseData.success("发布成功");
    }

    /**
     * 流程定义挂起/激活
     *
     * @param processDefinedId
     * @return
     */
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
     * 更换流程定义租户
     *
     * @param deploymentId 部署ID
     * @param tenantId     新租户
     * @return
     */
    @PutMapping(value = "/changeProcessDefinedTenant/{deploymentId}/{tenantId}")
    public ResponseData<String> changeProcessDefinedTenant(@PathVariable(value = "deploymentId") String deploymentId, @PathVariable(value = "tenantId") String tenantId) {
        repositoryService.changeDeploymentTenantId(deploymentId, tenantId);
        return ResponseData.success();
    }

    /**
     * 删除流程定义
     *
     * @param deploymentId 部署ID
     * @param isForce      是否强制删除
     * @return
     */
    @DeleteMapping(value = "/deleteProcessDefined/{deploymentId}")
    public ResponseData<String> deleteProcessDefined(@PathVariable(value = "deploymentId") String deploymentId, @RequestParam(value = "isForce", defaultValue = "false") boolean isForce) {
        if (isForce) {
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

    /**
     * 获取全部模型，带排序
     *
     * @param sort    排序：modifiedAsc，modifiedDesc，nameAsc，nameDesc
     * @param request
     * @return
     */
    @GetMapping(value = "/flowModels/{sort}")
    public ResponseData<List<ModelRepresentationVo>> flowModels(@PathVariable(value = "sort") String sort, HttpServletRequest request) {
        List<ModelRepresentation> modelList = (List<ModelRepresentation>) modelQueryService.getModels("processes", sort, 0, request).getData();
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
     * 获取全部模型
     *
     * @param request
     * @return
     */
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
     * 获取模型流程历史
     *
     * @param modelId
     * @return
     */
    @GetMapping(value = "/getModelHistory/{modelId}")
    public ResponseData<List<ModelRepresentation>> getModelHistory(@PathVariable(value = "modelId") String modelId) {
        List<ModelRepresentation> modelHistoryList = (List<ModelRepresentation>) modelHistoryResource.getModelHistoryCollection(modelId, false).getData();
        // 冒泡排序，更新时间最新的在最上面
        ModelRepresentation temp;
        if (modelHistoryList != null) {
            for (int i = 0; i < modelHistoryList.size(); i++) {
                for (int j = i + 1; j < modelHistoryList.size(); j++) {
                    if (modelHistoryList.get(i).getLastUpdated().compareTo(modelHistoryList.get(j).getLastUpdated()) < 0) {
                        temp = modelHistoryList.get(i);
                        modelHistoryList.set(i, modelHistoryList.get(j));
                        modelHistoryList.set(j, temp);
                    }
                }
            }
        }
        return ResponseData.success(modelHistoryList);
    }

    /**
     * 流程模型数据
     *
     * @return
     */
    @GetMapping(value = "/getFlowModel/{processDefinedId}")
    public ResponseData<List<Process>> getFlowModel(@PathVariable(value = "processDefinedId") String processDefinedId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinedId);
        return ResponseData.success(bpmnModel.getProcesses());
    }

    /**
     * 获取租户流程定义列表
     *
     * @param tenantList 租户列表
     * @return
     */
    @PostMapping(value = "/getProcessDefinedListByTenant")
    public ResponseData<List<ProcessDefinitionVo>> getProcessDefinedListByTenant(@RequestBody List<String> tenantList) {
        // 返回数据集
        List<ProcessDefinitionVo> processDefinitionVoList = new ArrayList<>();
        // 流程定义列表
        List<ProcessDefinition> processDefinitionList = new ArrayList<>();
        tenantList.forEach(item -> {
            // 获取各租户的流程定义列表
            processDefinitionList.addAll(repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(item).latestVersion().list());
        });
        processDefinitionList.forEach(processDefinition -> processDefinitionVoList.add(new ProcessDefinitionVo(processDefinition)));
        return ResponseData.success(processDefinitionVoList);
    }

    /**
     * 获取租户流程定义列表，带分页
     * @param flowableQueryEntity
     * @return
     */
    /*@PostMapping(value = "/getTenantProcessDefinedList")
    public ResponseData<String> getTenantProcessDefinedList(@RequestBody FlowableQueryEntity flowableQueryEntity) {
        // 页码
        Integer page = flowableQueryEntity.getPage();
        // 条数
        Integer limit = flowableQueryEntity.getLimit();
        // 租户列表
        List<String> tenantList = flowableQueryEntity.getTenantList();

        // 数据集
        List<ProcessDefinitionVo> processDefinitionVoList = new ArrayList<>();
        // 流程定义列表
        List<ProcessDefinition> processDefinitionList = new ArrayList<>();
        tenantList.forEach(item -> {
            // 获取各租户的流程定义列表
            processDefinitionList.addAll(repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(item).latestVersion().list());
        });
        processDefinitionList.forEach(processDefinition -> processDefinitionVoList.add(new ProcessDefinitionVo(processDefinition)));
        // 总条数
        Integer total = processDefinitionVoList.size();
        // 分页数据
        PageEntity pageEntity = new PageEntity(page, limit, total);
        // 进行分页后的数据
        List<ProcessDefinitionVo> data = new PageEntity<ProcessDefinitionVo>().pageData(page, limit, total, processDefinitionVoList);
        pageEntity.setData(data);
        return JsonUtil.toJSON(ErrorMsg.SUCCESS.setNewData(pageEntity));
    }*/

    /**
     * 获取流程定义所有用户任务节点列表
     *
     * @param processDefinedKey
     * @param tenant
     * @return
     */
    @GetMapping(value = "/getProcessDefinedTaskList/{processDefinedKey}/{tenant}")
    public ResponseData<List<Map<String, Object>>> getProcessDefinedTaskList(@PathVariable(value = "processDefinedKey") String processDefinedKey, @PathVariable(value = "tenant") String tenant) {
        List<Map<String, Object>> list = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinedKey).processDefinitionTenantId(tenant).latestVersion().singleResult();
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