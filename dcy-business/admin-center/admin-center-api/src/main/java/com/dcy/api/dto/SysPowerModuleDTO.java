package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权权限和模块
 * @Date: 2019/9/16 14:46
 */
@Data
public class SysPowerModuleDTO {
    /**
     * 权限id
     */
    private String powerId;
    /**
     * 模块ids
     */
    private List<String> moduleIds;
}
