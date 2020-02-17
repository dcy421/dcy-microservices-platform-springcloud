package com.dcy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;

/**
 * @author dcy
 * -javaagent:E:\Installation\skywalking\apache-skywalking-apm-6.6.0\agent\skywalking-agent.jar
 * -Dskywalking.agent.service_name=gateway-center
 * -Dproject.name=gateway-center
 * -Dcsp.sentinel.dashboard.server=127.0.0.1:8555
 * -Dcsp.sentinel.api.port=8720
 * -Dcsp.sentinel.app.type=1
 *
 * sentinel启动java -Dserver.port=8555 -Dcsp.sentinel.dashboard.server=localhost:8555 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.1.jar
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }
}
