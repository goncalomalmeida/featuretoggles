package com.experiments.toggles.configuration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    public static final String TOGGLE_EXCHANGE = "toggle.exchange.topic";

    @Bean
    public TopicExchange toggleExchange() {
        return new TopicExchange(TOGGLE_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
