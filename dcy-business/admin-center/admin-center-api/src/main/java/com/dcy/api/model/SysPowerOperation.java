package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 权限功能关联表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysPowerOperation对象", description = "权限功能关联表")
public class SysPowerOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    private String powId;

    @ApiModelProperty(value = "操作id")
    private String operId;


    public static final String POW_ID = "pow_id";

    public static final String OPER_ID = "oper_id";

}
