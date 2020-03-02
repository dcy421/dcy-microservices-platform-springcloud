package com.dcy.client;

import com.dcy.api.InterfaceService;
import com.dcy.api.model.SysUserInfo;
import com.dcy.api.service.SysUserInfoRemoteService;
import com.dcy.common.model.ResponseData;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author：dcy
 * @Description: 用户表暴露远程接口+熔断
 * @Date: 2020/3/2 13:26
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallbackFactory = SysUserInfoClientService.SysUserInfoServiceFallbackFactory.class)
public interface SysUserInfoClientService extends SysUserInfoRemoteService {


    @Component
    class SysUserInfoServiceFallbackFactory implements FallbackFactory<SysUserInfoClientService> {

        @Override
        public SysUserInfoClientService create(Throwable throwable) {
            return new SysUserInfoClientService() {
                @Override
                public ResponseData<SysUserInfo> getUserInfoByUsername(String username) {
                    return ResponseData.error("提供者出错了！");
                }

                @Override
                public ResponseData<Set<String>> getAllPermissionByUserId(String userId) {
                    return ResponseData.error("提供者出错了！");
                }
            };
        }
    }
}
