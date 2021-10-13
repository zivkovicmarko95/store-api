package com.store.storeproductapi.configurations;

import com.store.storesharedmodule.constants.EventsChannels;
import com.store.storesharedmodule.constants.EventsExchange;
import com.store.storesharedmodule.constants.EventsQueues;
import com.store.storesharedmodule.constants.EventsRoutingKeys;
import com.store.storesharedmodule.utils.RabbitmqUtils;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class AmqpConfig {

    @Bean
    public Queue authLoginQueue() {
        return new Queue(EventsQueues.AUTH_LOGIN_QUEUE.getKey());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EventsExchange.GLOBAL_EXCHANGE.getKey());
    }

    @Bean
    public Binding terminateBinding(final TopicExchange topicExchange) {
        return BindingBuilder.bind(authLoginQueue())
                .to(exchange())
                .with(EventsRoutingKeys.AUTH_LOGIN_ROUTING_KEY.getKey());
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        return RabbitmqUtils.buildAmqpTemplate(connectionFactory);
    }

    @Bean
    public IntegrationFlow amqpInboundAuthLogin(ConnectionFactory connectionFactory) {

        return RabbitmqUtils.amqpInbound(
            connectionFactory, 
            EventsQueues.AUTH_LOGIN_QUEUE.getKey(), 
            EventsRoutingKeys.AUTH_LOGIN_ROUTING_KEY.getKey(), 
            EventsChannels.AUTH_LOGIN_CHANNEL.getEventKey()
        );
    }

}
