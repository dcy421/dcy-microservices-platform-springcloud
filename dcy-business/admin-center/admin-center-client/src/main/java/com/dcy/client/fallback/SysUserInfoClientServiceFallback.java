package com.dcy.client.fallback;

import com.dcy.api.model.SysUserInfo;
import com.dcy.client.SysUserInfoClientService;
import com.dcy.common.model.ResponseData;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author：dcy
 * @Description: 用户表远程接口
 * @Date: 2019-10-14
 */
@Component
public class SysUserInfoClientServiceFallback implements SysUserInfoClientService {


    @Override
    public ResponseData<SysUserInfo> getUserInfoByUsername(String username) {
        return ResponseData.error("提供者出错了！");
    }

    @Override
    public ResponseData<Set<String>> getAllPermissionByUserId(String userId) {
        return ResponseData.error("提供者出错了！");
    }
}
