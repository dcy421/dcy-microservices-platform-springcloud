package com.dcy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户和用户组关联表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysUserInfoGroup对象", description = "用户和用户组关联表")
public class SysUserInfoGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户组id")
    private String userGroupId;


    public static final String USER_ID = "user_id";

    public static final String USER_GROUP_ID = "user_group_id";

}
