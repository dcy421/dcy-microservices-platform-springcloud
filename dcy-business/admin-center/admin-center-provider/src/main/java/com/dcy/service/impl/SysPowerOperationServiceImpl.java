package com.dcy.service.impl;

import com.dcy.api.model.SysPowerOperation;
import com.dcy.mapper.SysPowerOperationMapper;
import com.dcy.service.ISysPowerOperationService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 权限功能关联表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysPowerOperationServiceImpl extends BaseServiceImpl<SysPowerOperationMapper, SysPowerOperation> implements ISysPowerOperationService {

    @Autowired
    private SysPowerOperationMapper sysPowerOperationMapper;

}
