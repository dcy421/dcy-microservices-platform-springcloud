package com.dcy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.api.dto.SysPowerMenuDTO;
import com.dcy.api.dto.SysPowerModuleDTO;
import com.dcy.api.model.SysPower;
import com.dcy.api.model.SysPowerMenu;
import com.dcy.api.model.SysPowerModule;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.mapper.SysModuleResourcesMapper;
import com.dcy.mapper.SysPowerMapper;
import com.dcy.mapper.SysPowerMenuMapper;
import com.dcy.mapper.SysPowerModuleMapper;
import com.dcy.service.ISysPowerService;
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
 * 权限表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysPowerServiceImpl extends BaseServiceImpl<SysPowerMapper, SysPower> implements ISysPowerService {

    @Autowired
    private SysPowerMapper sysPowerMapper;
    @Autowired
    private SysPowerModuleMapper sysPowerModuleMapper;
    @Autowired
    private SysPowerMenuMapper sysPowerMenuMapper;
    @Autowired
    private SysModuleResourcesMapper sysModuleResourcesMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清空数据 方法调用后清空所有缓存
     *
     * @param sysPowerModuleDTO
     * @return
     */
    @Override
    public Boolean saveAuthModule(SysPowerModuleDTO sysPowerModuleDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysPowerModuleDTO.getPowerId()) && sysPowerModuleDTO.getModuleIds() != null) {
            // 删除关联表
            sysPowerModuleMapper.delete(new LambdaQueryWrapper<SysPowerModule>().eq(SysPowerModule::getPowId, sysPowerModuleDTO.getPowerId()));
            // 添加关联表
            sysPowerModuleDTO.getModuleIds().forEach(moduleId -> {
                sysPowerModuleMapper.insert(new SysPowerModule().setPowId(sysPowerModuleDTO.getPowerId()).setModuleId(moduleId));
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

    @Override
    public Boolean saveAuthMenu(SysPowerMenuDTO sysPowerMenuDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysPowerMenuDTO.getPowerId()) && sysPowerMenuDTO.getMenuIds() != null) {
            // 删除关联表
            sysPowerMenuMapper.delete(new LambdaQueryWrapper<SysPowerMenu>().eq(SysPowerMenu::getPowId, sysPowerMenuDTO.getPowerId()));
            // 添加关联表
            sysPowerMenuDTO.getMenuIds().forEach(menuId -> {
                sysPowerMenuMapper.insert(new SysPowerMenu().setPowId(sysPowerMenuDTO.getPowerId()).setMenuId(menuId));
            });
            success = true;
        }
        return success;
    }
}
