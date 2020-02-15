package com.dcy.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dcy
 * @since 2019-09-18
 */
@Data
@Accessors(chain = true)
@ApiModel(value="FileInfo对象", description="")
public class FileInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件md5")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private Boolean isImg;

    private String contentType;

    private Long size;

    @ApiModelProperty(value = "物理路径")
    private String path;

    private String url;

    private String source;

    private String createDate;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String IS_IMG = "is_img";

    public static final String CONTENT_TYPE = "content_type";

    public static final String SIZE = "size";

    public static final String PATH = "path";

    public static final String URL = "url";

    public static final String SOURCE = "source";

    public static final String CREATE_DATE = "createDate";



}
