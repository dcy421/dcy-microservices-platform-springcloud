package com.dcy.service;

import com.dcy.api.dto.TreeData;
import com.dcy.api.model.SysModuleResources;
import com.dcy.web.base.service.BaseService;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
public interface ISysModuleResourcesService extends BaseService<SysModuleResources> {

    /**
     * 获取tree-table 数据
     *
     * @return
     */
    List<SysModuleResources> getModuleTreeTableList();


    /**
     * 获取tree 数据
     *
     * @return
     */
    List<TreeData> getModuleTreeListByPowerId(String powerId);

    /**
     * 根据用户id 查询对应资源
     *
     * @param userId
     * @return
     */
    List<Map<String,Object>> getModuleByUserId(String userId);
}
