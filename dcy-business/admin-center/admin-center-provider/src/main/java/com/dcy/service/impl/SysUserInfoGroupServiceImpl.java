package com.dcy.service.impl;

import com.dcy.api.model.SysUserInfoGroup;
import com.dcy.mapper.SysUserInfoGroupMapper;
import com.dcy.service.ISysUserInfoGroupService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户和用户组关联表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysUserInfoGroupServiceImpl extends BaseServiceImpl<SysUserInfoGroupMapper, SysUserInfoGroup> implements ISysUserInfoGroupService {

    @Autowired
    private SysUserInfoGroupMapper sysUserInfoGroupMapper;

}
