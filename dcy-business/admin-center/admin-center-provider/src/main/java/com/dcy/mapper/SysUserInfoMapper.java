package com.dcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import com.dcy.api.model.SysUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getAuthRoleListByUserId(@Param("userId") String userId);


    /**
     * 根据用户id 查询已授权用户组列表
     *
     * @param userId
     * @return
     */
    List<SysUserGroup> getAuthGroupListByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    Set<String> getAuthRoleSetByUserId(@Param("userId") String userId);


    /**
     * 根据用户id 查询已授权菜单列表
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionListByUserId(@Param("userId") String userId);


    /**
     * 根据用户id 查询已授权的模块权限
     *
     * @param userId
     * @return
     */
    Set<String> getModuleListByUserId(@Param("userId") String userId);
}
