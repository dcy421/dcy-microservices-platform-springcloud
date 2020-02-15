package com.dcy.service.impl;

import com.dcy.api.model.SysRolePower;
import com.dcy.mapper.SysRolePowerMapper;
import com.dcy.service.ISysRolePowerService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色与权限关联表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysRolePowerServiceImpl extends BaseServiceImpl<SysRolePowerMapper, SysRolePower> implements ISysRolePowerService {

    @Autowired
    private SysRolePowerMapper sysRolePowerMapper;

}
