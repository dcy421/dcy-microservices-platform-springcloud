package com.dcy.service.impl;

import com.dcy.api.model.SysOperation;
import com.dcy.mapper.SysOperationMapper;
import com.dcy.service.ISysOperationService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 功能操作表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysOperationServiceImpl extends BaseServiceImpl<SysOperationMapper, SysOperation> implements ISysOperationService {

    @Autowired
    private SysOperationMapper sysOperationMapper;

}
