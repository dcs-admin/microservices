package org.kafka.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class KafkaMessage implements Serializable {
    private String key;
    private String message;
    private Integer partition;
    private Long timestamp;
    private long accountId;
    private String payloadType;
    private String payloadVersion;
    private Date actionEpoc;

    private ModelProperties modelProperties;
    private SystemProperties systemProperties;


    public KafkaMessage() {
         // fill with default values
        this.key = "key";
        this.message = "message";
        this.partition = 0;
        this.timestamp = 0L;
        this.modelProperties = new ModelProperties();
        this.systemProperties = new SystemProperties();
        this.accountId = 0L;
        this.payloadType = "payloadType";
        this.payloadVersion = "payloadVersion";
        this.actionEpoc = new Date();
    }



}
