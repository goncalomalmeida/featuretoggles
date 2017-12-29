package com.experiments.toggles.services.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <T> void send(String exchange, String routingKey, T payload, Map<String, String> headers) {
        final Message message = rabbitTemplate.getMessageConverter().toMessage(payload, new MessageProperties());
        headers.forEach((k, v) -> message.getMessageProperties().setHeader(k, v));

        // removes the default header from spring amqp
        message.getMessageProperties().getHeaders().remove(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME);

        rabbitTemplate.send(exchange, routingKey, message);
    }
}
