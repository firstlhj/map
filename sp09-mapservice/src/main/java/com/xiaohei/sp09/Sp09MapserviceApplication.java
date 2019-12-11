package com.xiaohei.sp09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javafx.application.Application;
@EnableScheduling
@SpringBootApplication
public class Sp09MapserviceApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(Sp09MapserviceApplication.class, args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Sp09MapserviceApplication.class);
    }

    
    
}
