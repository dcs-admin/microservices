package com.anji.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBenchmarkRunner implements CommandLineRunner {

    @Autowired
    private ConsumerPerformanceTest consumerPerformanceTest;

    @Override
    public void run(String... args) throws Exception {
        // The consumer will automatically start consuming messages when the application runs
    }
}