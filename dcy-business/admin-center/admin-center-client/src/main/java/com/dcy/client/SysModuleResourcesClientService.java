package com.dcy.client;

import com.dcy.api.InterfaceService;
import com.dcy.api.service.SysModuleResourcesRemoteService;
import com.dcy.client.fallback.SysModuleResourcesClientServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/3/2 13:27
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallback = SysModuleResourcesClientServiceFallback.class)
public interface SysModuleResourcesClientService extends SysModuleResourcesRemoteService {

}
