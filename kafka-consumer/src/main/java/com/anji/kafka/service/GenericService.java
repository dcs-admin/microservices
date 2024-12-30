package com.anji.kafka.service;

import org.kafka.shared.entity.CustomEntity;
import org.kafka.shared.entity.KafkaMessage;

public interface GenericService {

    public CustomEntity process(KafkaMessage message);
}
