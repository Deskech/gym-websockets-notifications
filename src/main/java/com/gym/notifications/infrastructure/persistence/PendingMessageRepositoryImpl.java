package com.gym.notifications.infrastructure.persistence;

import com.gym.notifications.domain.model.Coach;
import com.gym.notifications.domain.model.Message;
import com.gym.notifications.domain.repository.PendingMessageRepository;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class PendingMessageRepositoryImpl implements PendingMessageRepository {
    private final ReactiveHashOperations<String, String, String> reactiveRedisOperations;
    private final ReactiveRedisOperations<String, Object> objectReactiveRedisOperations;

    public PendingMessageRepositoryImpl(ReactiveHashOperations<String, String, String> reactiveRedisOperations, ReactiveRedisOperations<String, Object> objectReactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.objectReactiveRedisOperations = objectReactiveRedisOperations;
    }

    @Override
    public Mono<Void> savePendingMessage(Coach coachInformation, Message message) {
        String messageKey = "messageFor:" + coachInformation.coachName();
        String topicLink = "/admins/" + coachInformation.coachId() + "/notifications";


        Map<String, String> pendingMessage = new HashMap<>();
        pendingMessage.put("id", coachInformation.coachId());
        pendingMessage.put("topicLink", topicLink);
        pendingMessage.put("message", message.message());

        return reactiveRedisOperations.putAll(messageKey, pendingMessage)
                .then(objectReactiveRedisOperations.expire(messageKey, Duration.ofHours(10)))
                .then();
    }

    @Override
    public Mono<Void> deletePendingMessage(Coach coachInformation) {
        String messageKey = "messageFor:" + coachInformation.coachName();

        return objectReactiveRedisOperations.delete(messageKey).then();
    }
}
