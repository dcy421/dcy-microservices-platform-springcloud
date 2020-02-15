package com.dcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dcy.api.model.SysUserInfo;
import com.dcy.common.annotation.ValidResult;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.model.ResponseData;
import com.dcy.common.utils.BPwdEncoderUtil;
import com.dcy.service.ISysModuleResourcesService;
import com.dcy.service.ISysUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author dcy
 * @Date: 2019/3/22 10:17
 * @Description:
 */
@RestController
@Api(value = "LoginController", tags = {"登录操作接口"})
@Slf4j
public class LoginController {

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;
    @Autowired
    private ISysUserInfoService iSysUserInfoService;
    @Autowired
    private ISysModuleResourcesService iSysModuleResourcesService;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @GetMapping("/hello2")
    public String hello2() {
        return "hello you";
    }

    /**
     * 单点登录
     * 暂时不用
     *
     * @param sysUserInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
//    @SentinelResource("login")
    @ApiOperation(value = "登录")
    @ValidResult
    public ResponseData login(@RequestBody SysUserInfo sysUserInfo) throws Exception {
        // ----------------可以不调用------------------------
        SysUserInfo sysUserInfo1 = iSysUserInfoService.getOne(new QueryWrapper<SysUserInfo>().lambda().eq(SysUserInfo::getUsername, sysUserInfo.getUsername()));
        if (sysUserInfo1 == null) {
            throw new Exception("用户名和密码错误，请重新输入");
        }
        if ("1".equals(sysUserInfo1.getUserStatus())) {
            throw new Exception("用户已锁定");
        }
        if (!BPwdEncoderUtil.matches(sysUserInfo.getPassword(), sysUserInfo1.getPassword().replace("{bcrypt}", ""))) {
            throw new Exception("用户名和密码错误，请重新输入");
        }
        List<Map<String, Object>> moduleResourcesList = iSysModuleResourcesService.getModuleByUserId(sysUserInfo1.getUserId());
        // 存到redis中
        redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_MODULE_LIST_KEY + sysUserInfo1.getUserId(), moduleResourcesList);
        // ----------------可以不调用------------------------
        String client_secret = oAuth2ClientProperties.getClientId() + ":" + oAuth2ClientProperties.getClientSecret();

        client_secret = "Basic " + Base64.getEncoder().encodeToString(client_secret.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", client_secret);

        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", Collections.singletonList(sysUserInfo.getUsername()));
        map.put("password", Collections.singletonList(sysUserInfo.getPassword()));
        map.put("grant_type", Collections.singletonList(oAuth2ProtectedResourceDetails.getGrantType()));

        map.put("scope", oAuth2ProtectedResourceDetails.getScope());
        //HttpEntity
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);
        //获取 Token
        ResponseEntity<OAuth2AccessToken> exchange = restTemplate.exchange(oAuth2ProtectedResourceDetails.getAccessTokenUri(), HttpMethod.POST, httpEntity, OAuth2AccessToken.class);
        return ResponseData.success(exchange.getBody());
    }
}
