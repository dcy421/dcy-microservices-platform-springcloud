package com.dcy;

import com.dcy.config.AppDispatcherServletConfiguration;
import com.dcy.config.ApplicationConfiguration;
import org.flowable.ui.modeler.conf.DatabaseConfiguration;
import org.minbox.framework.api.boot.autoconfigure.swagger.annotation.EnableApiBootSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;


/**
 * @Author：dcy
 * @Description: 启动类
 * @Date: 2020-02-21 10:17
 * -javaagent:E:\Installation\skywalking\apache-skywalking-apm-6.6.0\agent\skywalking-agent.jar
 * -Dskywalking.agent.service_name=workflow-center
 */
@Import(value = {
        // 引入修改的配置
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
//        ,DatabaseConfiguration.class
})
@EnableDiscoveryClient
@EnableApiBootSwagger
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class WorkCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkCenterApplication.class, args);
    }

}

