package com.example.IP_Session_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class IpSession001Application {

	public static void main(String[] args) {
		SpringApplication.run(IpSession001Application.class, args);
	}

}
