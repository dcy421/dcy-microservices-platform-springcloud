package com.dcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.api.model.SysRole;
import com.dcy.api.model.SysUserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户组表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
public interface SysUserGroupMapper extends BaseMapper<SysUserGroup> {

    /**
     * 根据用户组id查询已授权的角色列表
     *
     * @param userGroupId
     * @return
     */
    List<SysRole> getAuthRoleListByUserGroupId(@Param("userGroupId") String userGroupId);
}
