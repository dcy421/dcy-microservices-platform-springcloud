package com.dcy.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dcy.common.model.ValidBaseInterface;
import com.dcy.db.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Set;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysUserInfo对象", description = "用户表")
public class SysUserInfo extends BaseModel implements ValidBaseInterface {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.ID_WORKER_STR)
    private String userId;

    @ApiModelProperty(value = "用户名")
    @TableField(condition = SqlCondition.LIKE)
    @NotNull
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * （0、管理员；1、普通用户）
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    /**
     * （0、男；1、女）
     */
    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "头像")
    private String avatarPath;

    /**
     * （0、正常；1、禁用）
     */
    @ApiModelProperty(value = "帐号状态")
    private String userStatus;

    /**
     * 所有的权限集
     */
    @TableField(exist = false)
    private Set<String> allPermissionSet;

    public static final String USER_ID = "user_id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String NICK_NAME = "nick_name";

    public static final String USER_TYPE = "user_type";

    public static final String EMAIL = "email";

    public static final String PHONE_NUMBER = "phone_number";

    public static final String SEX = "sex";

    public static final String AVATAR_PATH = "avatar_path";

    public static final String USER_STATUS = "user_status";


}
