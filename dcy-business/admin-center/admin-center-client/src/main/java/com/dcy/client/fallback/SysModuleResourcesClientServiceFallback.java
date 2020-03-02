package com.dcy.client.fallback;

import com.dcy.client.SysModuleResourcesClientService;
import com.dcy.common.model.ResponseData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-18 11:06
 */
@Component
public class SysModuleResourcesClientServiceFallback implements SysModuleResourcesClientService {

    @Override
    public ResponseData<List<Map<String, Object>>> getModuleByUserId(String userId) {
        return ResponseData.error("提供者出错了！");
    }
}
