package com.gym.notifications.infrastructure.adapters.input;

import com.gym.notifications.domain.factories.AdminFactory;
import com.gym.notifications.domain.model.Coach;
import com.gym.notifications.domain.repository.StatusRepository;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AdminsListener implements ChannelAwareMessageListener {
    private final StatusRepository statusRepository;

    private final AdminFactory factory;

    public AdminsListener(StatusRepository statusRepository, AdminFactory factory) {
        this.statusRepository = statusRepository;

        this.factory = factory;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //todo -> factory
        Coach coach = factory.createCoach(message.getBody());

        statusRepository.saveCoach(coach)
                .doOnSuccess(savedCoach -> {

                    try {
                        log.info("Message processed correctly for deliver tag: {}", deliveryTag);
                        channel.basicAck(deliveryTag, false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).doOnError(error -> {
                    try {
                        log.error("Failed to process message fro delivery tag: {}", deliveryTag, error);
                        channel.basicNack(deliveryTag, false, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).subscribe();

    }
}
