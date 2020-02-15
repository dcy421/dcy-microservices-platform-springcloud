package com.dcy.service.impl;

import com.dcy.api.model.SysUserGroupRole;
import com.dcy.mapper.SysUserGroupRoleMapper;
import com.dcy.service.ISysUserGroupRoleService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户组角色关联表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysUserGroupRoleServiceImpl extends BaseServiceImpl<SysUserGroupRoleMapper, SysUserGroupRole> implements ISysUserGroupRoleService {

    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;

}
