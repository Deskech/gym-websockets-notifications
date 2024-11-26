package com.gym.notifications.infrastructure.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.notifications.domain.factories.AdminFactory;
import com.gym.notifications.domain.model.Coach;
import com.gym.notifications.domain.status.CoachStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminFactoryImpl implements AdminFactory {
    private final ObjectMapper objectMapper;

    public AdminFactoryImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Coach createCoach(byte[] message) {

        try {
            JsonNode node = objectMapper.readTree(message);
            String id = node.get("id").asText();
            String name = node.get("name").asText();


            return new Coach(id, name, CoachStatus.OFFLINE);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }
}
