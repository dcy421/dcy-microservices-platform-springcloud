package com.dcy.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.dto.TreeData;
import com.dcy.api.model.SysMenu;
import com.dcy.api.model.SysPowerMenu;
import com.dcy.common.constant.CommonConstant;
import com.dcy.mapper.SysMenuMapper;
import com.dcy.mapper.SysPowerMenuMapper;
import com.dcy.service.ISysMenuService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysPowerMenuMapper sysPowerMenuMapper;
    private List<SysPowerMenu> sysPowerMenus;

    @Override
    public List<SysMenu> getMenuTreeTableList() {
        List<SysMenu> sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getLocation));
        List<SysMenu> treeDataList = new ArrayList<>();
        sysMenus.stream().forEach(sysMenu -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysMenu.getParentId())) {
                treeDataList.add(sysMenu);
            }
        });
        recursionTreeTableChildren(treeDataList, sysMenus);
        return treeDataList;
    }

    @Override
    public List<TreeData> getMenuTreeListByPowerId(String powerId) {
        // 获取所有的模块
        List<SysMenu> sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getLocation));
        sysPowerMenus = sysPowerMenuMapper.selectList(new LambdaQueryWrapper<SysPowerMenu>().eq(SysPowerMenu::getPowId, powerId));
        List<TreeData> treeDataList = new ArrayList<>();
        sysMenus.stream().forEach(sysMenu -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysMenu.getParentId())) {
                TreeData treeData = new TreeData();
                treeData.setId(sysMenu.getMenuId());
                treeData.setType(sysMenu.getType());
                treeData.setTitle(sysMenu.getMenuName());
                treeData.setExpand(Boolean.TRUE);
                treeDataList.add(treeData);
            }
        });
        recursionTreeChildren(treeDataList, sysMenus);
        return treeDataList;
    }

    private void recursionTreeTableChildren(List<SysMenu> treeDataList, List<SysMenu> sysModuleResources) {
        for (SysMenu treeData : treeDataList) {
            List<SysMenu> childrenList = new ArrayList<>();
            for (SysMenu sysMenu : sysModuleResources) {
                if (sysMenu.getParentId().equals(treeData.getMenuId())) {
                    childrenList.add(sysMenu);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, sysModuleResources);
            }
        }
    }

    private void recursionTreeChildren(List<TreeData> treeDataList, List<SysMenu> sysMenus) {
        for (TreeData treeData : treeDataList) {
            List<TreeData> childrenList = new ArrayList<>();
            for (SysMenu sysMenu : sysMenus) {
                if (sysMenu.getParentId().equals(treeData.getId())) {
                    TreeData treeData2 = new TreeData();
                    treeData2.setId(sysMenu.getMenuId());
                    treeData2.setExpand(Boolean.TRUE);
                    treeData2.setType(sysMenu.getType());
                    treeData2.setChecked(sysPowerMenus.stream().filter(sysPowerMenu -> sysPowerMenu.getMenuId().equalsIgnoreCase(sysMenu.getMenuId())).count() > 0);
                    treeData2.setTitle(sysMenu.getMenuName());
                    childrenList.add(treeData2);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeChildren(childrenList, sysMenus);
            }
        }
    }
}
