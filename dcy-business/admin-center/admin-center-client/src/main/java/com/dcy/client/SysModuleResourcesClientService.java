package com.dcy.client;

import com.dcy.api.InterfaceService;
import com.dcy.api.service.SysModuleResourcesRemoteService;
import com.dcy.common.model.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description: 用户表暴露远程接口+熔断
 * @Date: 2020/3/2 13:27
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallback = SysModuleResourcesClientService.SysModuleResourcesClientServiceFallback.class)
public interface SysModuleResourcesClientService extends SysModuleResourcesRemoteService {

    @Component
    class SysModuleResourcesClientServiceFallback implements SysModuleResourcesClientService{

        @Override
        public ResponseData<List<Map<String, Object>>> getModuleByUserId(String userId) {
            return ResponseData.error("提供者出错了！");
        }
    }
}
