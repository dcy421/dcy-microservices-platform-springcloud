package com.dcy.api.service;

import com.dcy.api.InterfaceService;
import com.dcy.api.model.SysUserInfo;
import com.dcy.api.service.fallback.SysUserInfoRemoteServiceFallback;
import com.dcy.common.model.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @Author：dcy
 * @Description: 用户表远程接口
 * @Date: 2019-10-14
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallback = SysUserInfoRemoteServiceFallback.class)
public interface SysUserInfoRemoteService {
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @GetMapping("/user/getUserInfoByUsername")
    ResponseData<SysUserInfo> getUserInfoByUsername(@RequestParam(value = "username") String username);

    /**
     * 根据用户id 查询已授权的权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/user/getAllPermissionByUserId")
    ResponseData<Set<String>> getAllPermissionByUserId(@RequestParam(value = "userId") String userId);

}
