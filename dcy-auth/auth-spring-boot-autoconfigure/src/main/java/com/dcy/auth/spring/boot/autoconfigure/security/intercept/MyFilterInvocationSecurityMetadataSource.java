package com.dcy.auth.spring.boot.autoconfigure.security.intercept;

import cn.hutool.core.map.MapUtil;
import com.dcy.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description: 用来动态获取url权限配置
 * @Date: 2019/9/12 13:29
 */
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TokenStore tokenStore;
    /**
     * 不拦截url
     */
    @Value("${ignored}")
    private List<String> ignored;

    private final Map<String, String> urlRoleMap = new HashMap<String, String>() {{
        // 开发使用
        put("/**", "ROLE_ADMIN");
    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        // 获取请求url
        String url = getUrl(fi.getRequestUrl());
        for (String igUrl : ignored) {
            if (antPathMatcher.match(igUrl, url)) {
                // 创建匿名权限
                return SecurityConfig.createList(CommonConstant.ROLE_ANONYMOUS);
            }
        }
        // 根据用户id 获取权限
        List<Map<String, Object>> moduleResourcesList = (List<Map<String, Object>>) redisTemplate.opsForValue().get(CommonConstant.REDIS_USER_MODULE_LIST_KEY + MapUtil.getStr(getUserInfo(), "userId"));
        for (Map<String, Object> moduleResources : moduleResourcesList) {
            // 判断是否符合url 和 method
            if (antPathMatcher.match(MapUtil.getStr(moduleResources, "modulePath"), url) && fi.getRequest().getMethod().equalsIgnoreCase(MapUtil.getStr(moduleResources, "httpMethod"))) {
                // 创建角色
                return SecurityConfig.createList(MapUtil.getStr(moduleResources, "roleKey"));
            }
        }
        //没有匹配到,默认是要登录才能访问 匿名权限
        return SecurityConfig.createList(CommonConstant.ROLE_ANONYMOUS);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 获取请求中的url
     *
     * @param url
     * @return
     */
    private String getUrl(String url) {
        //获取当前访问url
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            return url.substring(0, firstQuestionMarkIndex);
        }
        return url;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    private Map<String, Object> getUserInfo() {
        String tokenValue = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
        Map<String, Object> map = (Map<String, Object>) tokenStore.readAccessToken(tokenValue).getAdditionalInformation().get(CommonConstant.USER_INFO);
        return (Map<String, Object>) map.get("sysUserInfo");
    }

}
