package com.anji.approvals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import io.debezium.embedded.EmbeddedEngine;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import java.util.Properties;
import io.debezium.embedded.EmbeddedEngine.CompletionCallback;
import org.apache.kafka.connect.source.SourceRecord;
import java.util.function.Consumer;
import org.apache.kafka.connect.data.Struct;

@SpringBootApplication
@EnableEurekaClient
public class ProductServiceApplication {

	 
	//private EmbeddedEngine engine;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}


	// @Bean
    // public DisposableBean startDebeziumConnector() { 
	// 	Properties props = new Properties();
    //     props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
    //     props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    //     props.setProperty("offset.storage.file.filename", "/Users/vevana/anji/offset/file.txt");
    //     props.setProperty("offset.flush.interval.ms", "60000");
    //     props.setProperty("name", "mysql-connector");
    //     props.setProperty("database.hostname", "localhost");
    //     props.setProperty("database.port", "3306");
    //     props.setProperty("database.user", "root");
    //     props.setProperty("database.password", "p2wd1234@123");
    //     props.setProperty("database.server.id", "184054");
    //     props.setProperty("database.server.name", "mysql");
    //     props.setProperty("table.include.list", "itildesk1.test_table");
    //     props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
    //     props.setProperty("database.history.file.filename", "/Users/vevana/anjidbhistory.dat");


       
    //     CompletionCallback callback = new CompletionCallback() {
    //         @Override
    //         public void handle(boolean success, String errorMessage, Throwable t) {
    //             if (!success) {
    //                 System.err.println("Error occurred: " + errorMessage);
    //                 if (t != null) {
    //                     t.printStackTrace();
    //                 }
    //             }
    //         }
    //     };

    //     Consumer<SourceRecord> consumer = record -> {
    //         // Process the change event directly within your application
    //         System.out.println("Received event: " + record.key());

    //         Struct after = (Struct) ((Struct) record.value()).get("after");

    //         int id = after.getInt32("id");
    //         String name = after.getString("name");
    //         int age = after.getInt32("age");
     
    //         System.out.println("\t\t id: " + id +"; name: "+name+" ; age: "+age);
    //         //TODO Custom Bussiness logic here



            
    //     };

    //     EmbeddedEngine engine = (EmbeddedEngine) EmbeddedEngine.create()
    //             .using(props)
    //             .notifying(consumer)
    //             .build();
                
    //     System.out.println("\n\n\n\n\n\n==============DEBEZIUM MYSQL CONNECTOR DEMO=============\n\n\n\n\n\n" );
    //     engine.run();


    //     // Add a shutdown hook to stop the EmbeddedEngine gracefully
    //     return () -> {
    //         if (engine != null) {
	// 			System.out.println("STOPPED ");
    //             engine.stop();
    //         }
    //     };
    // }

}
