package com.dcy.auth.intercepter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.context.BaseContextHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author：dcy
 * @Description: token拦截器 获取用户信息
 * @Date: 2019/6/10 14:14
 */
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String authorization = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (StrUtil.isNotBlank(authorization)) {
            authorization = authorization.replaceFirst(CommonConstant.BEARER_TYPE, "");
            // 解析token
            DefaultClaims body = (DefaultClaims) Jwts.parser().setSigningKey(CommonConstant.SIGNING_KEY.getBytes()).parseClaimsJws(authorization).getBody();
            // 获取用户对象
            Map<String, Object> map = body.get(CommonConstant.USER_INFO, Map.class);
            Map<String, Object> sysUserInfo = (Map<String, Object>) map.get("sysUserInfo");
            BaseContextHandler.setUserID(MapUtil.getStr(sysUserInfo, "userId"));
            BaseContextHandler.setUsername(MapUtil.getStr(sysUserInfo, "username"));
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除变量
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
