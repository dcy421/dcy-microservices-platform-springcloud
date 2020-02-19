package com.dcy.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.model.SysModuleResources;
import com.dcy.api.model.SysPowerModule;
import com.dcy.common.constant.CommonConstant;
import com.dcy.mapper.SysModuleResourcesMapper;
import com.dcy.mapper.SysPowerModuleMapper;
import com.dcy.service.ISysModuleResourcesService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysModuleResourcesServiceImpl extends BaseServiceImpl<SysModuleResourcesMapper, SysModuleResources> implements ISysModuleResourcesService {

    @Autowired
    private SysModuleResourcesMapper sysModuleResourcesMapper;
    @Autowired
    private SysPowerModuleMapper sysPowerModuleMapper;

    @Override
    public List<SysModuleResources> getModuleTreeTableList() {
        List<SysModuleResources> sysModuleResources = sysModuleResourcesMapper.selectList(new LambdaQueryWrapper<SysModuleResources>().orderByAsc(SysModuleResources::getModuleSort));
        List<SysModuleResources> treeDataList = new ArrayList<>();
        sysModuleResources.stream().forEach(sysModuleResources1 -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysModuleResources1.getParentId())) {
                treeDataList.add(sysModuleResources1);
            }
        });
        recursionTreeTableChildren(treeDataList, sysModuleResources);
        return treeDataList;
    }

    @Override
    public List<String> getModuleTreeListByPowerId(String powerId) {
        return sysPowerModuleMapper.selectList(new LambdaQueryWrapper<SysPowerModule>().eq(SysPowerModule::getPowId, powerId)).stream().map(sysPowerModule -> sysPowerModule.getModuleId()).collect(Collectors.toList());
    }

    /**
     * 缓存数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String,Object>> getModuleByUserId(String userId) {
        return sysModuleResourcesMapper.getModuleByUserId(userId);
    }

    private void recursionTreeTableChildren(List<SysModuleResources> treeDataList, List<SysModuleResources> sysModuleResources) {
        for (SysModuleResources treeData : treeDataList) {
            List<SysModuleResources> childrenList = new ArrayList<>();
            for (SysModuleResources sysModuleResources1 : sysModuleResources) {
                if (sysModuleResources1.getParentId().equals(treeData.getModuleId())) {
                    childrenList.add(sysModuleResources1);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, sysModuleResources);
            }
        }
    }
}
