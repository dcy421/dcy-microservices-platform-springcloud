package com.dcy;

import com.dcy.file.properties.FileServerProperties;
import org.minbox.framework.api.boot.autoconfigure.swagger.annotation.EnableApiBootSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dcy
 * -javaagent:E:\Installation\skywalking\apache-skywalking-apm-6.6.0\agent\skywalking-agent.jar
 * -Dskywalking.agent.service_name=file-center
 */
@SpringBootApplication
@EnableConfigurationProperties(FileServerProperties.class)
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.dcy.file.mapper"})
@EnableApiBootSwagger
public class FileCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileCenterApplication.class, args);
    }

}
