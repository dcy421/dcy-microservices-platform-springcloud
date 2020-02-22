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
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "设置流程完成人")
    private String userId;

    @ApiModelProperty(value = "任务提交意见的内容")
    private String comment;

    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> variables;
}
