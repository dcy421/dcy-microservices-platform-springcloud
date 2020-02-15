package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户组角色关联表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysUserGroupRole对象", description = "用户组角色关联表")
public class SysUserGroupRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户组id")
    private String userGroupId;

    @ApiModelProperty(value = "角色id")
    private String roleId;


    public static final String USER_GROUP_ID = "user_group_id";

    public static final String ROLE_ID = "role_id";

}
