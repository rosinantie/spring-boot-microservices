package com.example.IP_Session_001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
=======
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
>>>>>>> 9cdcdc0 (Initial monorepo commit)
@EnableEurekaServer
public class IpSession001Application {

	public static void main(String[] args) {
		SpringApplication.run(IpSession001Application.class, args);
	}

}
