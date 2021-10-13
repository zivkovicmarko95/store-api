package com.store.storesharedmodule.utils;

import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;

public class RabbitmqUtils {
    
    private final static String AMQP_RECEIVED_ROUTING_KEY = "amqp_receivedRoutingKey";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module());

    private RabbitmqUtils() {

    }

    /**
     * AMQP inbound
     * 
     * @param connectionFactory connection factory
     * @param queue queue
     * @param routingKey events routing key
     * @param channelKey channel key
     * @return 
     */
    public static final IntegrationFlow amqpInbound(ConnectionFactory connectionFactory, final String queue, 
            final Object routingKey, final String channelKey) {

        ArgumentVerifier.verifyNotNull(queue, routingKey, channelKey);

        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, queue))
                .log(LoggingHandler.Level.INFO, new LiteralExpression("AMQP message recieved ..."))
                .route(
                    Message.class,
                    message -> message.getHeaders().get(AMQP_RECEIVED_ROUTING_KEY),
                    message -> {
                        message.channelMapping(routingKey, channelKey);
                    }
                )
                .get();
    }

    /**
     * Build an AMPQ template based on Rabbit MQ
     * 
     * @param connectionFactory connection factory
     * @return amqp template
     */
    public static final AmqpTemplate buildAmqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(OBJECT_MAPPER));
        
        return rabbitTemplate;
    }
}
