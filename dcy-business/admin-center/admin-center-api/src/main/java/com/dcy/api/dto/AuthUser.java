package com.dcy.api.dto;

import com.dcy.api.model.SysUserInfo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-14 16:22
 */
@Data
public class AuthUser implements UserDetails {

    private SysUserInfo sysUserInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        // 角色权限集
        for (String grantedAuthority : sysUserInfo.getAllPermissionSet()) {
            auths.add(new SimpleGrantedAuthority(grantedAuthority));
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return sysUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUserInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
