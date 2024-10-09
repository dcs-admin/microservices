package com.anji.approvals.connector;

import io.debezium.embedded.EmbeddedEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.anji.approvals.connector.DebeziumConnector;

import org.springframework.context.annotation.ComponentScan;

@Component
@ComponentScan(basePackages = "com.anji.approvals")
public class DebeziumConnector implements ApplicationListener<ApplicationReadyEvent> {

    private final EmbeddedEngine engine;

    @Autowired
    public DebeziumConnector(EmbeddedEngine engine) {
        this.engine = engine;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Start Debezium EmbeddedEngine when the application is ready
        System.out.println("\n\n\n\n\n\n============== DEBEZIUM MYSQL CDC ENABLED =============\n\n\n\n\n\n" );
        engine.run();
    }

    // Add a method to stop the engine gracefully
    public void stop() {
        System.out.println("\n\n\n\n\n\n============== DEBEZIUM MYSQL CDC STOPPED =============\n\n\n\n\n\n" );
        engine.stop();
    }
}
