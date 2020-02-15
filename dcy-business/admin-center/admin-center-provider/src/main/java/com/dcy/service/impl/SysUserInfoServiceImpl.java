package com.dcy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.dto.SysUserInfoGroupDTO;
import com.dcy.api.dto.SysUserInfoRoleDTO;
import com.dcy.api.model.*;
import com.dcy.common.constant.CommonConstant;
import com.dcy.mapper.SysModuleResourcesMapper;
import com.dcy.mapper.SysUserInfoGroupMapper;
import com.dcy.mapper.SysUserInfoMapper;
import com.dcy.mapper.SysUserRoleMapper;
import com.dcy.service.ISysUserInfoService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysUserInfoServiceImpl extends BaseServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserInfoGroupMapper sysUserInfoGroupMapper;
    @Autowired
    private SysModuleResourcesMapper sysModuleResourcesMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<SysRole> getAuthRoleListByUserId(String userId) {
        return sysUserInfoMapper.getAuthRoleListByUserId(userId);
    }

    /**
     * 删除缓存
     *
     * @param sysUserInfoRoleDTO
     * @return
     */
    @Override
    public boolean saveAuthRole(SysUserInfoRoleDTO sysUserInfoRoleDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysUserInfoRoleDTO.getUserId()) && sysUserInfoRoleDTO.getRoleIds() != null) {
            // 删除关联表
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUserInfoRoleDTO.getUserId()));
            // 添加关联表
            sysUserInfoRoleDTO.getRoleIds().forEach(roleId -> {
                sysUserRoleMapper.insert(new SysUserRole().setUserId(sysUserInfoRoleDTO.getUserId()).setRoleId(roleId));
            });
            success = true;
        }
        if (success) {
            // 删除缓存
            redisTemplate.delete(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfoRoleDTO.getUserId());
            // 在查询权限
            List<Map<String, Object>> moduleResourcesList = sysModuleResourcesMapper.getModuleByUserId(sysUserInfoRoleDTO.getUserId());
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfoRoleDTO.getUserId(), moduleResourcesList);
        }
        return success;
    }

    @Override
    public List<SysUserGroup> getAuthGroupListByUserId(String userId) {
        return sysUserInfoMapper.getAuthGroupListByUserId(userId);
    }

    /**
     * 清空数据 方法调用后清空所有缓存
     *
     * @param sysUserInfoGroupDTO
     * @return
     */
    @Override
    public boolean saveAuthGroup(SysUserInfoGroupDTO sysUserInfoGroupDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysUserInfoGroupDTO.getUserId()) && sysUserInfoGroupDTO.getUserGroupIds() != null) {
            // 删除关联表
            sysUserInfoGroupMapper.delete(new LambdaQueryWrapper<SysUserInfoGroup>().eq(SysUserInfoGroup::getUserId, sysUserInfoGroupDTO.getUserId()));
            // 添加关联表
            sysUserInfoGroupDTO.getUserGroupIds().forEach(userGroupId -> {
                sysUserInfoGroupMapper.insert(new SysUserInfoGroup().setUserId(sysUserInfoGroupDTO.getUserId()).setUserGroupId(userGroupId));
            });
            success = true;
        }
        if (success) {
            // 删除缓存
            redisTemplate.delete(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfoGroupDTO.getUserId());
            // 在查询权限
            List<Map<String, Object>> moduleResourcesList = sysModuleResourcesMapper.getModuleByUserId(sysUserInfoGroupDTO.getUserId());
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfoGroupDTO.getUserId(), moduleResourcesList);
        }
        return success;
    }

    @Override
    public Set<String> getAuthRoleSetByUserId(String userId) {
        return sysUserInfoMapper.getAuthRoleSetByUserId(userId);
    }

    @Override
    public Set<String> getPermissionListByUserId(String userId) {
        return sysUserInfoMapper.getPermissionListByUserId(userId);
    }

    @Override
    public Set<String> getModuleListByUserId(String userId) {
        return sysUserInfoMapper.getModuleListByUserId(userId);
    }
}
