package com.dcy.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-22 11:11
 */
@Data
public class ProcessInstanceDTO {

    /**
     * 部署的流程 key，来自 ACT_RE_PROCDEF
     */
    private String processDefinitionKey;

    /**
     * 数据 Key，业务键，一般为表单数据的 ID，仅作为表单数据与流程实例关联的依据
     */
    private String businessKey;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;

    /**
     * 用户 Id
     */
    private String userId;


}

