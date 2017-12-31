package com.experiments.toggles.services.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes a message <i>payload</i> to the given <i>exchange</i> using a <i>routingKey</i> and some <i>headers</i>
     *
     * @param exchange   the RabbitMQ's exchange to publish to
     * @param routingKey routing key to use
     * @param payload    payload that must be serializable to JSON
     * @param headers    a map containing all the headers that should be carried along with the message
     * @param <T>        ensure that the payloads is serializable
     */
    public <T extends Serializable> void send(String exchange, String routingKey, T payload, Map<String, String> headers) {
        final Message message = rabbitTemplate.getMessageConverter().toMessage(payload, new MessageProperties());

        // sets up all the headers
        headers.forEach((k, v) -> message.getMessageProperties().setHeader(k, v));

        // removes the default header from spring amqp
        message.getMessageProperties().getHeaders().remove(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME);

        // publishes the message
        rabbitTemplate.send(exchange, routingKey, message);
    }
}
