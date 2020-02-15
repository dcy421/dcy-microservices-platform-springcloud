package com.dcy.security.auth.controller;

import com.alibaba.fastjson.JSON;
import com.dcy.api.dto.AuthUser;
import com.dcy.api.model.SysUserInfo;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-14 13:32
 */
@RestController
public class AuthController {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 暴露Remote Token Services接口
     * 获取登录信息
     * 如果其它服务需要验证Token，则需要远程调用授权服务暴露的验证Token的API接口
     *
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseData<SysUserInfo> getUser() {
        String tokenValue = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
        Object userInfoMap = tokenStore.readAccessToken(tokenValue).getAdditionalInformation().get(CommonConstant.USER_INFO);
        return ResponseData.success(JSON.parseObject(JSON.toJSONString(userInfoMap), AuthUser.class).getSysUserInfo());
    }

    /**
     * 获取OAuth用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getOAuthDetails", method = RequestMethod.GET)
    public OAuth2Authentication getOAuthDetails(OAuth2Authentication user) {
        return user;
    }
}
