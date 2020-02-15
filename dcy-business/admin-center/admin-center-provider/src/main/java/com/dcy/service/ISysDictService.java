package com.dcy.service;

import com.dcy.api.model.SysDict;
import com.dcy.web.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysDictService extends BaseService<SysDict> {


    /**
     * 根据分组类型查询字典项tree
     *
     * @param groupType
     * @return
     */
    List<SysDict> getDictTreeListByGroupType(String groupType);
}
