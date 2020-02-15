package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权用户组使用
 * @Date: 2019/9/12 10:02
 */
@Data
public class SysUserInfoGroupDTO {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 授权用户组Ids
     */
    private List<String> userGroupIds;
}
