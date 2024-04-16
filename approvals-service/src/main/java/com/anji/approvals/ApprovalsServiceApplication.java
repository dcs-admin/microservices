package com.anji.approvals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import io.debezium.embedded.EmbeddedEngine;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;

import com.anji.approvals.connector.DebeziumConnector;

import java.util.Properties;
import io.debezium.embedded.EmbeddedEngine.CompletionCallback;
import org.apache.kafka.connect.source.SourceRecord;
import java.util.function.Consumer;
import org.apache.kafka.connect.data.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PreDestroy;


@SpringBootApplication
@EnableEurekaClient
public class ApprovalsServiceApplication {

	 
    @Autowired
    private DebeziumConnector debeziumConnector;

	public static void main(String[] args) {
		SpringApplication.run(ApprovalsServiceApplication.class, args);
	} 
 
     // Add a shutdown hook to stop the Debezium connector gracefully
     @PreDestroy
     public void onExit() {
         debeziumConnector.stop();
     }



   
}
