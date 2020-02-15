package com.dcy.service;

import com.dcy.api.dto.SysUserInfoGroupDTO;
import com.dcy.api.dto.SysUserInfoRoleDTO;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import com.dcy.api.model.SysUserInfo;
import com.dcy.web.base.service.BaseService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysUserInfoService extends BaseService<SysUserInfo> {

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getAuthRoleListByUserId(String userId);


    /**
     * 保存授权角色
     *
     * @param sysUserInfoRoleDTO
     * @return
     */
    boolean saveAuthRole(SysUserInfoRoleDTO sysUserInfoRoleDTO);

    /**
     * 根据用户id 查询已授权用户组列表
     *
     * @param userId
     * @return
     */
    List<SysUserGroup> getAuthGroupListByUserId(String userId);

    /**
     * 保存授权用户组
     *
     * @param sysUserInfoGroupDTO
     * @return
     */
    boolean saveAuthGroup(SysUserInfoGroupDTO sysUserInfoGroupDTO);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    Set<String> getAuthRoleSetByUserId(String userId);


    /**
     * 根据用户id 查询已授权菜单列表
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionListByUserId(String userId);


    /**
     * 根据用户id 查询已授权的模块权限
     *
     * @param userId
     * @return
     */
    Set<String> getModuleListByUserId(String userId);
}
