package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TestingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingSystemApplication.class, args);
	}

}
