package com.dcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.api.model.SysModuleResources;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
public interface SysModuleResourcesMapper extends BaseMapper<SysModuleResources> {

    /**
     * 根据用户id 查询对应资源
     *
     * @param userId
     * @return
     */
    List<Map<String,Object>> getModuleByUserId(@Param("userId") String userId);
}
