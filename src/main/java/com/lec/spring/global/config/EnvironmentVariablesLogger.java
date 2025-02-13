package com.lec.spring.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentVariablesLogger implements CommandLineRunner {

    @Value("${SPRING_MYSQL_URL}")
    private String mysqlUrl;

    @Value("${SPRING_MYSQL_USERNAME}")
    private String mysqlUsername;

    @Value("${SPRING_MYSQL_PASSWORD}")
    private String mysqlPassword;

    @Value("${SPRING_MONGODB_URI}")
    private String mongodbUri;


    public EnvironmentVariablesLogger() {
        System.out.println("EnvironmentVariablesLogger() 생성");
        System.out.println("SPRING_MYSQL_URL: " + mysqlUrl);
        System.out.println("SPRING_MYSQL_USERNAME: " + mysqlUsername);
        System.out.println("SPRING_MYSQL_PASSWORD: " + mysqlPassword);
        System.out.println("SPRING_MONGODB_URI: " + mongodbUri);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("SPRING_MYSQL_URL: " + mysqlUrl);
        System.out.println("SPRING_MYSQL_USERNAME: " + mysqlUsername);
        System.out.println("SPRING_MYSQL_PASSWORD: " + mysqlPassword);
        System.out.println("SPRING_MONGODB_URI: " + mongodbUri);
    }
}