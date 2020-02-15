package com.dcy.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dcy.db.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysRole对象", description = "角色表")
public class SysRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @TableId(value = "role_id", type = IdType.ID_WORKER_STR)
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    @TableField(condition = SqlCondition.LIKE)
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    /**
     * （0、正常；1、禁用）
     */
    @ApiModelProperty(value = "角色状态")
    private String roleStatus;


    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

    public static final String ROLE_KEY = "role_key";

    public static final String ROLE_STATUS = "role_status";


}
