package com.dcy.service.impl;

import com.dcy.api.model.SysPowerModule;
import com.dcy.mapper.SysPowerModuleMapper;
import com.dcy.service.ISysPowerModuleService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-16
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysPowerModuleServiceImpl extends BaseServiceImpl<SysPowerModuleMapper, SysPowerModule> implements ISysPowerModuleService {

    @Autowired
    private SysPowerModuleMapper sysPowerModuleMapper;

}
