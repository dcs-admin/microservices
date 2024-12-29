package com.kafka.shared.entity;

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
    private ModelProperties modelProperties;
    private SystemProperties systemProperties;
    private long accountId;
    private String payloadType;
    private String payloadVersion;
    private String payloadCategory;
    private String payloadDescription;
    private String payloadStatus;
    private String payloadOwner;
    private String payloadCreatedDate;
    private String payloadModifiedDate;
    private String payloadPublishedDate;
    private String payloadPublishedBy;
    private String payloadPublishedVersion;
    private String payloadPublishedStatus;
    private String payloadPublishedDescription;
    private String payloadPublishedDateFrom;
    private String payloadPublishedDateTo;
    private String payloadPublishedByFrom;
    private String payloadPublishedByTo;
    private String payloadPublishedVersionFrom;
    private String payloadPublishedVersionTo;
    private String payloadPublishedStatusFrom;
    private String payloadPublishedStatusTo;
    private String payloadPublishedDescriptionFrom;
    private String payloadPublishedDescriptionTo;
    private String payloadPublishedDateFromTo;
    private String payloadPublishedByFromTo;
    private String payloadPublishedVersionFromTo;
    private String payloadPublishedStatusFromTo;
    private String payloadPublishedDescriptionFromTo;
    private String payloadPublishedDateFromToFrom;
    private String payloadPublishedByFromToFrom;
    private String payloadPublishedVersionFromToFrom;
    private String payloadPublishedStatusFromToFrom;
    private Date actionEpoc;

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
        this.payloadCategory = "payloadCategory";
        this.payloadDescription = "payloadDescription";
        this.payloadStatus = "payloadStatus";
        this.payloadOwner = "payloadOwner";
        this.payloadCreatedDate = "payloadCreatedDate";
        this.payloadModifiedDate = "payloadModifiedDate";
        this.payloadPublishedDate = "payloadPublishedDate";
        this.payloadPublishedBy = "payloadPublishedBy";
        this.payloadPublishedVersion = "payloadPublishedVersion";
        this.payloadPublishedStatus = "payloadPublishedStatus";
        this.payloadPublishedDescription = "payloadPublishedDescription";
        this.payloadPublishedDateFrom = "payloadPublishedDateFrom";
        this.payloadPublishedDateTo = "payloadPublishedDateTo";
        this.payloadPublishedByFrom = "payloadPublishedByFrom";
        this.payloadPublishedByTo = "payloadPublishedByTo";
        this.payloadPublishedVersionFrom = "payloadPublishedVersionFrom";
        this.payloadPublishedVersionTo = "payloadPublishedVersionTo";
        this.payloadPublishedStatusFrom = "payloadPublishedStatusFrom";
        this.payloadPublishedStatusTo = "payloadPublishedStatusTo";
        this.payloadPublishedDescriptionFrom = "payloadPublishedDescriptionFrom";
        this.payloadPublishedDescriptionTo = "payloadPublishedDescriptionTo";
        this.payloadPublishedDateFromTo = "payloadPublishedDateFromTo";
        this.payloadPublishedByFromTo = "payloadPublishedByFromTo";
        this.payloadPublishedVersionFromTo = "payloadPublishedVersionFromTo";
        this.payloadPublishedStatusFromTo = "payloadPublishedStatusFromTo";
        this.payloadPublishedDescriptionFromTo = "payloadPublishedDescriptionFromTo";
        this.payloadPublishedDateFromToFrom = "payloadPublishedDateFromToFrom";
        this.payloadPublishedByFromToFrom = "payloadPublishedByFromToFrom";
        this.payloadPublishedVersionFromToFrom = "payloadPublishedVersionFromToFrom";
        this.payloadPublishedStatusFromToFrom = "payloadPublishedStatusFromToFrom";
        this.actionEpoc = new Date();
    }

}
