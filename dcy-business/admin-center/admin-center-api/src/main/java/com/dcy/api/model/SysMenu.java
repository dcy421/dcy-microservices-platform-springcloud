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
 * 菜单表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysMenu对象", description = "菜单表")
public class SysMenu extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单id")
    @TableId(value = "menu_id", type = IdType.ID_WORKER_STR)
    private String menuId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ApiModelProperty(value = "上级ids")
    private String parentIds;

    @ApiModelProperty(value = "权限code")
    private String menuCode;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "地址")
    private String href;

    @ApiModelProperty(value = "打开方式")
    private Integer target;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "禁用")
    private String disabledStatus;

    @ApiModelProperty(value = "排序")
    private BigDecimal location;

    @ApiModelProperty(value = "是否有子节点")
    private Integer hasChildren;

    @TableField(exist = false)
    private List<SysMenu> children;

    public static final String MENU_ID = "menu_id";

    public static final String MENU_NAME = "menu_name";

    public static final String PARENT_ID = "parent_id";

    public static final String PARENT_IDS = "parent_ids";

    public static final String MENU_CODE = "menu_code";

    public static final String TYPE = "type";

    public static final String HREF = "href";

    public static final String TARGET = "target";

    public static final String ICON = "icon";

    public static final String DISABLED_STATUS = "disabled_status";

    public static final String LOCATION = "location";

    public static final String HAS_CHILDREN = "has_children";

}
