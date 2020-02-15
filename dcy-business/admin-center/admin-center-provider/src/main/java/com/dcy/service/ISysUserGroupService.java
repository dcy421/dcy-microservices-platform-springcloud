package com.dcy.service;

import com.dcy.api.dto.SysUserGroupRoleDTO;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import com.dcy.web.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 用户组表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysUserGroupService extends BaseService<SysUserGroup> {

    /**
     * 根据用户组id查询已授权的角色列表
     *
     * @param userGroupId
     * @return
     */
    List<SysRole> getAuthRoleListByUserGroupId(String userGroupId);

    /**
     * 保存授权角色
     *
     * @param sysUserGroupRoleDTO
     * @return
     */
    boolean saveAuthRole(SysUserGroupRoleDTO sysUserGroupRoleDTO);
}
