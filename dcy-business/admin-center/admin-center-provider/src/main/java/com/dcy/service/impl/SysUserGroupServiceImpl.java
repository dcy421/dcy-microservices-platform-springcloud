package com.dcy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.dto.SysUserGroupRoleDTO;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import com.dcy.api.model.SysUserGroupRole;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.mapper.SysModuleResourcesMapper;
import com.dcy.mapper.SysUserGroupMapper;
import com.dcy.mapper.SysUserGroupRoleMapper;
import com.dcy.service.ISysUserGroupService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户组表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysUserGroupServiceImpl extends BaseServiceImpl<SysUserGroupMapper, SysUserGroup> implements ISysUserGroupService {

    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;
    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;
    @Autowired
    private SysModuleResourcesMapper sysModuleResourcesMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<SysRole> getAuthRoleListByUserGroupId(String userGroupId) {
        return sysUserGroupMapper.getAuthRoleListByUserGroupId(userGroupId);
    }

    @Override
    public boolean saveAuthRole(SysUserGroupRoleDTO sysUserGroupRoleDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysUserGroupRoleDTO.getUserGroupId()) && sysUserGroupRoleDTO.getRoleIds() != null) {
            // 删除关联表
            sysUserGroupRoleMapper.delete(new LambdaQueryWrapper<SysUserGroupRole>().eq(SysUserGroupRole::getUserGroupId, sysUserGroupRoleDTO.getUserGroupId()));
            // 添加关联表
            sysUserGroupRoleDTO.getRoleIds().forEach(roleId -> {
                sysUserGroupRoleMapper.insert(new SysUserGroupRole().setUserGroupId(sysUserGroupRoleDTO.getUserGroupId()).setRoleId(roleId));
            });
            success = true;
        }
        if (success){
            String userId = BaseContextHandler.getUserID();
            // 删除缓存
            redisTemplate.delete(CommonConstant.REDIS_USER_MODULE_LIST_KEY +userId);
            // 在查询权限
            List<Map<String,Object>> moduleResourcesList = sysModuleResourcesMapper.getModuleByUserId(userId);
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY+userId,moduleResourcesList);
        }
        return success;
    }
}
