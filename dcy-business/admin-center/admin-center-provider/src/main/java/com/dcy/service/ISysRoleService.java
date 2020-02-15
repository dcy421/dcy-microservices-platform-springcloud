package com.dcy.service;

import com.dcy.api.dto.SysRolePowerDTO;
import com.dcy.api.model.SysPower;
import com.dcy.api.model.SysRole;
import com.dcy.web.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysRoleService extends BaseService<SysRole> {

    /**
     * 根据角色id查询已授权的权限列表
     *
     * @param roleId
     * @return
     */
    List<SysPower> getAuthPowerListByRoleId(String roleId);

    /**
     * 保存授权权限
     *
     * @param sysRolePowerDTO
     * @return
     */
    Boolean saveAuthPower(SysRolePowerDTO sysRolePowerDTO);
}
