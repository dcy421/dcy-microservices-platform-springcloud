package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysPowerModule对象", description = "")
public class SysPowerModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    private String powId;

    @ApiModelProperty(value = "模块id")
    private String moduleId;


    public static final String POW_ID = "pow_id";

    public static final String MODULE_ID = "module_id";

}
