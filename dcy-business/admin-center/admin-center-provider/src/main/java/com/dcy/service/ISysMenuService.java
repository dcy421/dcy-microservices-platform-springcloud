package com.dcy.service;

import com.dcy.api.dto.TreeData;
import com.dcy.api.model.SysMenu;
import com.dcy.web.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysMenuService extends BaseService<SysMenu> {


    /**
     * 获取tree-table列表数据
     *
     * @return
     */
    List<SysMenu> getMenuTreeTableList();

    /**
     * 获取tree 数据
     *
     * @param powerId
     * @return
     */
    List<TreeData> getMenuTreeListByPowerId(String powerId);
}
