package com.anji.kafka.service;

import com.anji.kafka.entity.CustomEntity;
import com.anji.kafka.entity.KafkaMessage;

public interface GenericService {

    public CustomEntity process(KafkaMessage message);
}
