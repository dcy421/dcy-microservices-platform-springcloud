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
 * 功能操作表
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysOperation对象", description = "功能操作表")
public class SysOperation extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作id")
    @TableId(value = "oper_id", type = IdType.ID_WORKER_STR)
    private String operId;

    @ApiModelProperty(value = "操作名字")
    private String operName;

    @ApiModelProperty(value = "操作代码")
    private String operCode;

    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ApiModelProperty(value = "上级ids")
    private String parentIds;


    public static final String OPER_ID = "oper_id";

    public static final String OPER_NAME = "oper_name";

    public static final String OPER_CODE = "oper_code";

    public static final String PARENT_ID = "parent_id";

    public static final String PARENT_IDS = "parent_ids";

}
