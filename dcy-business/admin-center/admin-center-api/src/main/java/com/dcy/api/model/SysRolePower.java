package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色与权限关联表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysRolePower对象", description = "角色与权限关联表")
public class SysRolePower implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "权限id")
    private String powId;


    public static final String ROLE_ID = "role_id";

    public static final String POW_ID = "pow_id";

}
