package com.dcy.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.model.SysDict;
import com.dcy.common.constant.CommonConstant;
import com.dcy.mapper.SysDictMapper;
import com.dcy.service.ISysDictService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public List<SysDict> getDictTreeListByGroupType(String groupType) {
        // 获取所有字典项
        List<SysDict> dictList = sysDictMapper.selectList(new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictType, groupType).orderByAsc(SysDict::getLocation));
        List<SysDict> treeDataList = new ArrayList<>();
        dictList.stream().forEach(sysDict -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysDict.getParentId())) {
                treeDataList.add(sysDict);
            }
        });
        recursionTreeTableChildren(treeDataList, dictList);
        return treeDataList;
    }


    @Override
    public List<SysDict> getDictTreeTableList() {
        // 获取所有字典项
        List<SysDict> dictList = sysDictMapper.selectList(new LambdaQueryWrapper<SysDict>().orderByAsc(SysDict::getLocation));
        List<SysDict> treeDataList = new ArrayList<>();
        dictList.stream().forEach(sysDict -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysDict.getParentId())) {
                treeDataList.add(sysDict);
            }
        });
        recursionTreeTableChildren(treeDataList, dictList);
        return treeDataList;
    }

    private void recursionTreeTableChildren(List<SysDict> treeDataList, List<SysDict> dictList) {
        for (SysDict treeData : treeDataList) {
            List<SysDict> childrenList = new ArrayList<>();
            for (SysDict sysDict : dictList) {
                if (sysDict.getParentId().equals(treeData.getDictId())) {
                    childrenList.add(sysDict);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, dictList);
            }
        }
    }
}
