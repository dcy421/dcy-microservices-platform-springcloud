package com.dcy.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.stream.Collectors;

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
    public List<String> getMenuTreeListByPowerId(String powerId) {
        return sysPowerMenuMapper.selectList(new LambdaQueryWrapper<SysPowerMenu>().eq(SysPowerMenu::getPowId, powerId)).stream().map(sysPowerMenu -> sysPowerMenu.getMenuId()).collect(Collectors.toList());
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
}
