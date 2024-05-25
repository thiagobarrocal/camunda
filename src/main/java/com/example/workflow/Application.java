package com.example.workflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition
@EnableFeignClients
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

}