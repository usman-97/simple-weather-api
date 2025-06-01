package com.simple.weather.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan("com.simple.weather.api.application")
public class Application {
	public static void main(String[] args)
	{
		log.info("Staring simple weather api ...");
		SpringApplication.run(Application.class, args);
	}
}
