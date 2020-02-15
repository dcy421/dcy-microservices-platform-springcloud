package com.dcy.client;

import com.dcy.api.InterfaceService;
import com.dcy.api.service.SysModuleResourcesRemoteService;
import com.dcy.common.model.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-15 14:37
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallback = SysModuleResourcesClientService.SysModuleResourcesClientServiceFallback.class)
public interface SysModuleResourcesClientService extends SysModuleResourcesRemoteService {

    class SysModuleResourcesClientServiceFallback implements SysModuleResourcesClientService{

        @Override
        public ResponseData<List<Map<String, Object>>> getModuleByUserId(String userId) {
            return ResponseData.error("提供者出错了！");
        }
    }
}
