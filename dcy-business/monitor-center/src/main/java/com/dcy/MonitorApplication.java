package com.dcy;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dcy
 * -javaagent:E:\Installation\skywalking\apache-skywalking-apm-6.6.0\agent\skywalking-agent.jar
 * -Dskywalking.agent.service_name=monitor-center
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}
