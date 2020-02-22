package com.dcy.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-22 11:11
 */
@Data
public class TaskDTO {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 设置流程完成人
     */
    private String userId;
    /**
     * 任务提交意见的内容
     */
    private String comment;
    /**
     * 流程变量
     */
    private Map<String, Object> variables;
}
