package org.optmistic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class OptimisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(OptimisticApplication.class, args);
    }
}