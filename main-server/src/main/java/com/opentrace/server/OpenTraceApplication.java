package com.opentrace.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.opentrace.server",
		"com.opentrace.shared"
})
public class OpenTraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenTraceApplication.class, args);
	}
}
