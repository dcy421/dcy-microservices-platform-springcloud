package com.dcy.config;

import com.dcy.auth.intercepter.UserAuthRestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dcy
 * @date 2017/9/8
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // token拦截器 获取用户信息
        registry.addInterceptor(new UserAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }

}
