package com.dcy.security.auth.controller;

import com.alibaba.fastjson.JSON;
import com.dcy.api.model.SysUserInfo;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.model.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-14 13:32
 */
@RestController
@Api(value = "AuthController", tags = {"鉴权操作接口"})
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private TokenStore tokenStore;

    /**
     * 重写 /auth/token 方法
     *
     * @param principal
     * @param parameters
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @ApiOperation(value = "重写 /auth/token 方法", notes = "重写 /auth/token 方法")
    @GetMapping("/oauth/token")
    public ResponseData<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return ResponseData.success(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    /**
     * 重写 /auth/token 方法
     *
     * @param principal
     * @param parameters
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @ApiOperation(value = "重写 /auth/token 方法", notes = "重写 /auth/token 方法")
    @PostMapping("/oauth/token")
    public ResponseData<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return ResponseData.success((tokenEndpoint.postAccessToken(principal, parameters).getBody()));
    }

    /**
     * 暴露Remote Token Services接口
     * 获取登录信息
     * 如果其它服务需要验证Token，则需要远程调用授权服务暴露的验证Token的API接口
     *
     * @return
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseData<SysUserInfo> getUser() {
        String tokenValue = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
        Object userInfoMap = tokenStore.readAccessToken(tokenValue).getAdditionalInformation().get(CommonConstant.USER_INFO);
        return ResponseData.success(JSON.parseObject(JSON.toJSONString(userInfoMap), SysUserInfo.class));
    }

    /**
     * 获取OAuth用户信息
     *
     * @param principal
     * @return
     */
    @ApiOperation(value = "获取OAuth用户信息", notes = "获取OAuth用户信息")
    @RequestMapping(value = "/getOAuthDetails", method = RequestMethod.GET)
    public Principal getOAuthDetails(Principal principal) {
        return principal;
    }
}
