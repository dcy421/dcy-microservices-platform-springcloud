package com.dcy.client;

import com.dcy.api.InterfaceService;
import com.dcy.api.service.SysUserInfoRemoteService;
import com.dcy.client.fallback.SysUserInfoClientServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/3/2 13:26
 */
@FeignClient(name = InterfaceService.SERVICE_NAME, fallback = SysUserInfoClientServiceFallback.class)
public interface SysUserInfoClientService extends SysUserInfoRemoteService {

}
