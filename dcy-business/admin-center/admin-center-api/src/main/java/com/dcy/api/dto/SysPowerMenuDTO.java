package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权权限和菜单
 * @Date: 2019/9/24 9:53
 */
@Data
public class SysPowerMenuDTO {
    /**
     * 权限id
     */
    private String powerId;
    /**
     * 菜单ids
     */
    private List<String> menuIds;
}
