package com.store.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    Source source;

    public void sendUserId(Long userId){
        Message<Long> message = MessageBuilder.withPayload(userId).build();
        source.output().send(message);
    }
}
