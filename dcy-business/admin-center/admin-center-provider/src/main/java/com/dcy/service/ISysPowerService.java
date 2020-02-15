package com.dcy.service;

import com.dcy.api.dto.SysPowerMenuDTO;
import com.dcy.api.dto.SysPowerModuleDTO;
import com.dcy.api.model.SysPower;
import com.dcy.web.base.service.BaseService;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface ISysPowerService extends BaseService<SysPower> {

    /**
     * 保存授权模块
     *
     * @param sysPowerModuleDTO
     * @return
     */
    Boolean saveAuthModule(SysPowerModuleDTO sysPowerModuleDTO);

    /**
     * 保存授权菜单
     *
     * @param sysPowerMenuDTO
     * @return
     */
    Boolean saveAuthMenu(SysPowerMenuDTO sysPowerMenuDTO);
}
