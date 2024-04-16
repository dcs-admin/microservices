package com.anji.approvals.configuration;

import io.debezium.embedded.EmbeddedEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anji.approvals.service.ApprovalsConsumerService;

import java.util.Properties;

import io.debezium.embedded.EmbeddedEngine.CompletionCallback;

@Configuration
public class DebeziumConfiguration {
    

    @Autowired
    private ApprovalsConsumerService approvalsConsumer;

    @Bean
    public EmbeddedEngine embeddedEngine() {
        Properties props = new Properties();
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "/Users/vevana/anji/offset/file.txt");
        props.setProperty("offset.flush.interval.ms", "60000");
        props.setProperty("name", "mysql-connector");
        props.setProperty("database.hostname", "localhost");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "p2wd1234@123");
        props.setProperty("database.server.id", "184054");
        props.setProperty("database.server.name", "mysql");
        props.setProperty("table.include.list", "itildesk1.itil_approvals_custom");
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "/Users/vevana/anjidbhistory.dat"); 
       
        CompletionCallback callback = new CompletionCallback() {
            @Override
            public void handle(boolean success, String errorMessage, Throwable t) {
                if (!success) {
                    System.err.println("Error occurred: " + errorMessage);
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            }
        }; 

        EmbeddedEngine engine = (EmbeddedEngine) EmbeddedEngine.create()
                .using(props)
                .notifying(approvalsConsumer.buildConsumer())
                .build(); 
        //engine.run();

        return engine;
    }
 
}
