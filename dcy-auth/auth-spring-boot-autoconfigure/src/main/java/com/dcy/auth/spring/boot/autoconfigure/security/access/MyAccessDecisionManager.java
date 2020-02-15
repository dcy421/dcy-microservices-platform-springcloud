package com.dcy.auth.spring.boot.autoconfigure.security.access;

import cn.hutool.core.collection.CollUtil;
import com.dcy.common.constant.CommonConstant;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author：dcy
 * @Description: 进行权限判断。
 * @Date: 2019/9/12 13:28
 */
public class MyAccessDecisionManager implements AccessDecisionManager {


    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (CollUtil.isEmpty(configAttributes)) {
            throw new AccessDeniedException("not allow");
        }
        for (ConfigAttribute ca : configAttributes) {
            String needRole = ca.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                // 增加匿名权限不拦截
                if (ga.getAuthority().equals(needRole) || CommonConstant.ROLE_ANONYMOUS.equalsIgnoreCase(needRole)) {
                    //匹配到有对应角色,则允许通过
                    return;
                }
            }
        }
        //该url有配置权限,但是当然登录用户没有匹配到对应权限,则禁止访问
        throw new AccessDeniedException("not allow");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
