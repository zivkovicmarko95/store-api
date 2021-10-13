package com.store.storeauthapi.configs;

import com.store.storesharedmodule.utils.RabbitmqUtils;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AmqpConfig {
    
    @Bean
    @Primary
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        return RabbitmqUtils.buildAmqpTemplate(connectionFactory);
    }

}
