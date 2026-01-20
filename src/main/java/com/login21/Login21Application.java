package com.login21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy

public class Login21Application {

	public static void main(String[] args) {
		SpringApplication.run(Login21Application.class, args);
	}

}
