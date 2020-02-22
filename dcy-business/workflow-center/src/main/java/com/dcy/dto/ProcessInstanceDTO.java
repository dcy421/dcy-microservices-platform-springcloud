package com.dcy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-22 11:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ProcessInstanceDTO对象", description = "流程定义查询提交对象")
public class ProcessInstanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部署的流程 key",notes = "部署的流程 key，来自 ACT_RE_PROCDEF")
    private String processDefinitionKey;


    @ApiModelProperty(value = "数据 Key，业务键",notes = "数据 Key，业务键，一般为表单数据的 ID，仅作为表单数据与流程实例关联的依据")
    private String businessKey;


    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> processVariables;


    @ApiModelProperty(value = "设置发起人")
    private String userId;


}

