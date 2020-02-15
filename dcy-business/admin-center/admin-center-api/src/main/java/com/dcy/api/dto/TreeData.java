package com.dcy.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/16 8:58
 */
@Data
public class TreeData {
    /**
     * id
     */
    private String id;
    /**
     * 类型
     */
    private String type;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否展开直子节点
     */
    private Boolean expand = Boolean.FALSE;
    /**
     * 禁掉响应
     */
    private Boolean disabled = Boolean.FALSE;
    /**
     * 禁掉 checkbox
     */
    private Boolean disableCheckbox = Boolean.FALSE;
    /**
     * 是否选中子节点
     */
    private Boolean selected = Boolean.FALSE;
    /**
     * 是否勾选(如果勾选，子节点也会全部勾选)
     */
    private Boolean checked = Boolean.FALSE;
    /**
     * 子节点属性数组
     */
    private List<TreeData> children;

}
