package com.dcy.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dcy.db.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysPower对象", description = "权限表")
public class SysPower extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    @TableId(value = "pow_id", type = IdType.ID_WORKER_STR)
    private String powId;

    @ApiModelProperty(value = "权限名称")
    private String powName;

    /**
     * （0、模块权限；1、菜单权限）
     */
    @ApiModelProperty(value = "权限类型")
    private String powType;

    /**
     * （0、正常；1、禁用）
     */
    @ApiModelProperty(value = "权限状态")
    private String powStatus;


    public static final String POW_ID = "pow_id";

    public static final String POW_NAME = "pow_name";

    public static final String POW_TYPE = "pow_type";

    public static final String POW_STATUS = "pow_status";

}
