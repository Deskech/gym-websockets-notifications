package com.gym.notifications.infrastructure.config;

import com.gym.notifications.infrastructure.adapters.input.AdminsListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("adminUsers", true, false);
    }

    @Bean
    public Queue adminsQueue() {
        return new Queue("adminsQueue", true);
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue adminsQueue) {
        return BindingBuilder.bind(adminsQueue).to(exchange).with("admins");
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(ConnectionFactory factory,
                                                            MessageListenerAdapter adapter
    ) {

 SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(factory);
            container.setQueueNames("adminsQueue");
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
            container.setMaxConcurrentConsumers(1);
            container.setMessageListener(adapter);
            return container;
    }

    @Bean
    public MessageListenerAdapter adapter(AdminsListener adminsListener) {
        return new MessageListenerAdapter(adminsListener,"onMessage");
    }
}
