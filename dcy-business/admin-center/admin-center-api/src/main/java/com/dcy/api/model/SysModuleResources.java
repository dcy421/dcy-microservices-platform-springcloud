package com.dcy.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dcy.db.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysModuleResources对象", description = "")
public class SysModuleResources extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "module_id", type = IdType.ID_WORKER_STR)
    private String moduleId;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "父级ids")
    private String parentIds;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "模块code")
    private String moduleCode;

    @ApiModelProperty(value = "模块path")
    private String modulePath;

    @ApiModelProperty(value = "请求方式")
    private String httpMethod;

    @ApiModelProperty(value = "状态")
    private String moduleStatus;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private BigDecimal sort;

    @TableField(exist = false)
    private List<SysModuleResources> children;

    @TableField(exist = false)
    private String roleKey;

    public static final String MODULE_ID = "module_id";

    public static final String PARENT_ID = "parent_id";

    public static final String PARENT_IDS = "parent_ids";

    public static final String MODULE_NAME = "module_name";

    public static final String MODULE_CODE = "module_code";

    public static final String MODULE_PATH = "module_path";

    public static final String HTTP_METHOD = "http_method";

    public static final String MODULE_STATUS = "module_status";

    public static final String TYPE = "type";

    public static final String SORT = "sort";

}
