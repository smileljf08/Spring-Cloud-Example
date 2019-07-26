package com.example.serviceone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class ServiceOneApplication {


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(ServiceOneApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/one")
    public String one() {
        return "service-one: " + port;
    }

    @RequestMapping(value = "loadInstance")
    public String loadInstance() {
        ServiceInstance choose = this.loadBalancerClient.choose("service-one");
        System.out.println(choose.getServiceId()+":"+choose.getHost()+":"+choose.getPort());
        return choose.getServiceId() + ":" + choose.getHost() + ":" + choose.getPort();
    }
}
