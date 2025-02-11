package com.gdu.wacdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class WacdoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WacdoApplication.class, args);
	}

}
