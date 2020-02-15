package com.dcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.api.model.SysPower;
import com.dcy.api.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * 根据角色id查询已授权的权限列表
     *
     * @param roleId
     * @return
     */
    List<SysPower> getAuthPowerListByRoleId(@Param("roleId") String roleId);
}
