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
 * 字典类型表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysDict对象", description = "字典类型表")
public class SysDict extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典主键")
    @TableId(value = "dict_id", type = IdType.ID_WORKER_STR)
    private String dictId;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "父级ids")
    private String parentIds;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "字典名称")
    private String dictLable;

    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @ApiModelProperty(value = "排序")
    private BigDecimal location;

    @ApiModelProperty(value = "子节点")
    private Integer hasChildren;

    @ApiModelProperty(value = "子类型")
    private String type;

    /**
     * 子数据
     */
    @TableField(exist = false)
    private List<SysDict> children;

    public static final String DICT_ID = "dict_id";

    public static final String PARENT_ID = "parent_id";

    public static final String PARENT_IDS = "parent_ids";

    public static final String DICT_TYPE = "dict_type";

    public static final String DICT_LABLE = "dict_lable";

    public static final String DICT_VALUE = "dict_value";

    public static final String LOCATION = "location";

    public static final String HAS_CHILDREN = "has_children";

    public static final String TYPE = "type";

}
