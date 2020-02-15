package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权权限使用
 * @Date: 2019/9/16 11:08
 */
@Data
public class SysRolePowerDTO {
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 授权权限Ids
     */
    private List<String> powerIds;
}
