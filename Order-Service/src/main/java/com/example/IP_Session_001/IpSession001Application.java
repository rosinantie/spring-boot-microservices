package com.example.IP_Session_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD

@SpringBootApplication
=======
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
>>>>>>> 9cdcdc0 (Initial monorepo commit)
public class IpSession001Application {

	public static void main(String[] args) {
		SpringApplication.run(IpSession001Application.class, args);
	}

}
