package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 权限菜单关联表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysPowerMenu对象", description = "权限菜单关联表")
public class SysPowerMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单id")
    private String menuId;

    @ApiModelProperty(value = "权限id")
    private String powId;


    public static final String MENU_ID = "menu_id";

    public static final String POW_ID = "pow_id";

}
