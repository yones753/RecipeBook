package com.zion.service;


import com.zion.bean.AuthMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerService {

    @Autowired
     KafkaTemplate<String, AuthMessage> kafkaTemplate;

    private final static String SMS_TOPIC = "securityTopics";


    public void sendMessage(AuthMessage notificationMessage) {
        kafkaTemplate.send(SMS_TOPIC, notificationMessage);
    }

}
