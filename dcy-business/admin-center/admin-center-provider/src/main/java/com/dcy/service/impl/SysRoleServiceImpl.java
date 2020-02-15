package com.dcy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.dto.SysRolePowerDTO;
import com.dcy.api.model.SysPower;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysRolePower;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.mapper.SysModuleResourcesMapper;
import com.dcy.mapper.SysRoleMapper;
import com.dcy.mapper.SysRolePowerMapper;
import com.dcy.service.ISysRoleService;
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
 * 角色表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePowerMapper sysRolePowerMapper;
    @Autowired
    private SysModuleResourcesMapper sysModuleResourcesMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<SysPower> getAuthPowerListByRoleId(String roleId) {
        return sysRoleMapper.getAuthPowerListByRoleId(roleId);
    }

    /**
     * 清空数据 方法调用后清空所有缓存
     *
     * @param sysRolePowerDTO
     * @return
     */
    @Override
    public Boolean saveAuthPower(SysRolePowerDTO sysRolePowerDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysRolePowerDTO.getRoleId()) && sysRolePowerDTO.getPowerIds() != null) {
            // 删除关联表
            sysRolePowerMapper.delete(new LambdaQueryWrapper<SysRolePower>().eq(SysRolePower::getRoleId, sysRolePowerDTO.getRoleId()));
            // 添加关联表
            sysRolePowerDTO.getPowerIds().forEach(powerId -> {
                sysRolePowerMapper.insert(new SysRolePower().setRoleId(sysRolePowerDTO.getRoleId()).setPowId(powerId));
            });
            success = true;
        }
        if (success) {
            String userId = BaseContextHandler.getUserID();
            // 删除缓存
            redisTemplate.delete(CommonConstant.REDIS_USER_MODULE_LIST_KEY + userId);
            // 在查询权限
            List<Map<String, Object>> moduleResourcesList = sysModuleResourcesMapper.getModuleByUserId(userId);
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY + userId, moduleResourcesList);
        }
        return success;
    }
}
