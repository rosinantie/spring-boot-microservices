package com.example.IP_Session_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IpSession001Application {

	public static void main(String[] args) {
		SpringApplication.run(IpSession001Application.class, args);
	}

}
