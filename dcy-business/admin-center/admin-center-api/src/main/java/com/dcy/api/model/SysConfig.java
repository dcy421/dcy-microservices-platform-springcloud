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
 * 参数配置表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysConfig对象", description = "参数配置表")
public class SysConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数id")
    @TableId(value = "config_id", type = IdType.ID_WORKER_STR)
    private String configId;

    @ApiModelProperty(value = "参数名称")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "系统内置")
    private Integer configType;


    public static final String CONFIG_ID = "config_id";

    public static final String CONFIG_NAME = "config_name";

    public static final String CONFIG_KEY = "config_key";

    public static final String CONFIG_VALUE = "config_value";

    public static final String CONFIG_TYPE = "config_type";

}
