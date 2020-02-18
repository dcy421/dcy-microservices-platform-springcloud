package com.dcy.api.service.fallback;

import com.dcy.api.InterfaceService;
import com.dcy.api.model.SysUserInfo;
import com.dcy.api.service.SysUserInfoRemoteService;
import com.dcy.common.model.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @Author：dcy
 * @Description: 用户表远程接口
 * @Date: 2019-10-14
 */
@Component
public class SysUserInfoRemoteServiceFallback implements SysUserInfoRemoteService {


    @Override
    public ResponseData<SysUserInfo> getUserInfoByUsername(String username) {
        return ResponseData.error("提供者出错了！");
    }

    @Override
    public ResponseData<Set<String>> getAllPermissionByUserId(String userId) {
        return ResponseData.error("提供者出错了！");
    }
}
