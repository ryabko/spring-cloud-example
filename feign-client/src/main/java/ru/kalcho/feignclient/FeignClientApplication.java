package ru.kalcho.feignclient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
@RestController
public class FeignClientApplication {

    @Autowired
    private HelloClient helloClient;

    @Autowired
    private EurekaClient eurekaClient;

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Feign Client: " + helloClient.hello();
    }

    @RequestMapping("/info")
    public String info() {

        Application application =
                eurekaClient.getApplication("spring-cloud-eureka-client");
        List<InstanceInfo> instances = application.getInstances();

        InstanceInfo instanceInfo = instances.get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        String instanceId = instanceInfo.getInstanceId();

        return String.format("Eureka Client info: hostname=%s, port=%d, instanceId=%s " +
                        "(total instances = %d)", hostname, port, instanceId, instances.size());
    }
}
