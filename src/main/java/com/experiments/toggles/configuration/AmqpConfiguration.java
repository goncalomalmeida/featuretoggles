package com.experiments.toggles.configuration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link AmqpConfiguration} sets up the minimum amount of AMQP pieces that this project needs:
 * <u>
 *     <li>declares an exchange so that it can be created if it doesn't exist</li>
 *     <li>declares a message converter {@link Jackson2JsonMessageConverter} to tell spring amqp that the messages should be
 *     serialized/deserialized using Jackson (JSON) </li>
 * </u>
 */
@Configuration
public class AmqpConfiguration {

    public static final String TOGGLE_EXCHANGE = "toggle.exchange.topic";

    /**
     * Declares a <i>topic</i> exchange so that multiple consumers can listen from it.
     * Creates the exchange if it doesn't exist
     * @return a {@link TopicExchange} with the given name
     */
    @Bean
    public TopicExchange toggleExchange() {
        return new TopicExchange(TOGGLE_EXCHANGE);
    }

    /**
     *
     * @return a {@link MessageConverter} that spring amqp will use when dealing with message payloads
     */
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
