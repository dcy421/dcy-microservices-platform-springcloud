package com.dcy.service.impl;

import com.dcy.api.model.SysPowerMenu;
import com.dcy.mapper.SysPowerMenuMapper;
import com.dcy.service.ISysPowerMenuService;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 权限菜单关联表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysPowerMenuServiceImpl extends BaseServiceImpl<SysPowerMenuMapper, SysPowerMenu> implements ISysPowerMenuService {

    @Autowired
    private SysPowerMenuMapper sysPowerMenuMapper;

}
