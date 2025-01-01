package com.anji.kafka.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


//@SpringBootTest
public class BenchmarkRunner {
//    implements CommandLineRunner

    @Autowired
    private ProducerPerformanceTest producerPerformanceTest;

    @MockBean
    private ApplicationKafkaProducer applicationKafkaProducer;

    //@Override
    public void run(String... args) throws Exception {
        System.out.println("Starting benchmark...");
        producerPerformanceTest.runBenchmark(100); // Number of messages to send
    }

    @Test
    public void testRunBenchmark() throws Exception {
        // Mock the sendMessage method
        doNothing().when(applicationKafkaProducer).sendMessage(anyString());

        // Run the benchmark
        run();

        // Verify that sendMessage was called
        verify(applicationKafkaProducer, times(100)).sendMessage(anyString());
    }
}