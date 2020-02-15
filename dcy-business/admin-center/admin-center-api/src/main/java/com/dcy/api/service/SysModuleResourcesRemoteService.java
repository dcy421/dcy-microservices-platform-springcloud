package com.dcy.api.service;

import com.dcy.api.model.SysUserInfo;
import com.dcy.common.model.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-15 14:36
 */
public interface SysModuleResourcesRemoteService {

    /**
     * 根据用户id 查询对应资源
     *
     * @param userId
     * @return
     */
    @GetMapping("/module/getModuleByUserId")
    ResponseData<List<Map<String,Object>>> getModuleByUserId(@RequestParam(value = "userId") String userId);
}
