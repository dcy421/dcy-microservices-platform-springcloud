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
 * 用户组表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysUserGroup对象", description = "用户组表")
public class SysUserGroup extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户组id")
    @TableId(value = "user_group_id", type = IdType.ID_WORKER_STR)
    private String userGroupId;

    @ApiModelProperty(value = "用户组名")
    @TableField(condition = SqlCondition.LIKE)
    private String userGroupName;


    public static final String USER_GROUP_ID = "user_group_id";

    public static final String USER_GROUP_NAME = "user_group_name";


}
