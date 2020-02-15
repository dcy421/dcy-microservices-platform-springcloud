package com.dcy.service;

import com.dcy.api.model.SysConfig;
import com.dcy.web.base.service.BaseService;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysConfigService extends BaseService<SysConfig> {

    /**
     * 根据配置key查询配置value
     *
     * @param key
     * @return
     */
    String getValueByKey(String key);
}
