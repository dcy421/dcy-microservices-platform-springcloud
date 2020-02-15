package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/12 10:58
 */
@Data
public class SysUserGroupRoleDTO {
    /**
     * 用户组Id
     */
    private String userGroupId;
    /**
     * 授权角色Ids
     */
    private List<String> roleIds;
}
