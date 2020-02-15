package com.dcy.security.auth.service;

import com.dcy.api.dto.AuthUser;
import com.dcy.api.model.SysUserInfo;
import com.dcy.api.service.SysModuleResourcesRemoteService;
import com.dcy.api.service.SysUserInfoRemoteService;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author dcy
 * @Date: 2019/3/19 16:31
 * @Description:
 */
@Service
public class UserServiceDetailImpl implements UserDetailsService {

    @Autowired
    private SysUserInfoRemoteService sysUserInfoRemoteService;
    @Autowired
    private SysModuleResourcesRemoteService sysModuleResourcesRemoteService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Basic ZGN5X2FkbWluX2NsaWVudDoxMjM0NTY=
        //查询用户信息
        ResponseData<SysUserInfo> userInfoByUsername = sysUserInfoRemoteService.getUserInfoByUsername(username);
        if (userInfoByUsername.getSuccess()) {
            SysUserInfo sysUserInfo = userInfoByUsername.getData();
            // 查询权限
            ResponseData<Set<String>> allPermissionByUserId = sysUserInfoRemoteService.getAllPermissionByUserId(sysUserInfo.getUserId());
            if (allPermissionByUserId.getSuccess()) {
                ResponseData<List<Map<String, Object>>> moduleResourcesListData = sysModuleResourcesRemoteService.getModuleByUserId(sysUserInfo.getUserId());
                if (moduleResourcesListData.getSuccess()){
                    // 存到redis中
                    redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfo.getUserId(), moduleResourcesListData.getData());
                }
                AuthUser authUser = new AuthUser();
                authUser.setSysUserInfo(sysUserInfo);
                sysUserInfo.setAllPermissionSet(allPermissionByUserId.getData());
                return authUser;
            }
            return null;
        }
        return null;
    }
}
