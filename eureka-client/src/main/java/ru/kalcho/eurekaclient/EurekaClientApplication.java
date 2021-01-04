package ru.kalcho.eurekaclient;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EurekaClientApplication {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}}")
    private String defaultInstanceId;

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return String.format("Hello from '%s'! instanceId = %s. Default instanceId would be %s",
                eurekaClient.getApplication(appName).getName(),
                eurekaClient.getApplicationInfoManager().getInfo().getInstanceId(),
                defaultInstanceId);
    }
}
