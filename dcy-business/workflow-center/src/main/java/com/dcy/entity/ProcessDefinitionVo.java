package com.dcy.entity;

import lombok.Data;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * ProcessDefinitionVo
 * @author: linjinp
 * @create: 2020-01-02 14:08
 **/
@Data
public class ProcessDefinitionVo {

    private String id;

    private String key;

    private String name;

    private String category;

    private String description;

    private int version;

    private String deploymentId;

    private String tenantId;

    private String derivedFrom;

    private String derivedFromRoot;

    private int derivedVersion;

    private String diagramResourceName;

    private String engineVersion;

    private String resourceName;

    private Boolean isSuspended;

    private Boolean hasGraphicalNotation;

    private Boolean hasStartFormKey;

    private String deploymentDate;

    public ProcessDefinitionVo(){}

    public ProcessDefinitionVo(ProcessDefinition processDefinition) {
        id = processDefinition.getId();
        key = processDefinition.getKey();
        name = processDefinition.getName();
        category = processDefinition.getCategory();
        description = processDefinition.getDescription();
        version = processDefinition.getVersion();
        deploymentId = processDefinition.getDeploymentId();
        tenantId = processDefinition.getTenantId();
        derivedFrom = processDefinition.getDerivedFrom();
        derivedFromRoot = processDefinition.getDerivedFromRoot();
        derivedVersion = processDefinition.getDerivedVersion();
        diagramResourceName = processDefinition.getDiagramResourceName();
        engineVersion = processDefinition.getEngineVersion();
        resourceName = processDefinition.getResourceName();
        isSuspended = processDefinition.isSuspended();
        hasGraphicalNotation = processDefinition.hasGraphicalNotation();
        hasStartFormKey = processDefinition.hasStartFormKey();
    }
}
