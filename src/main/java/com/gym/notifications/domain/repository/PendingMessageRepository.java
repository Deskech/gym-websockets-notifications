package com.gym.notifications.domain.repository;

import com.gym.notifications.domain.model.Coach;
import com.gym.notifications.domain.model.Message;
import reactor.core.publisher.Mono;

public interface PendingMessageRepository {

    Mono<Void> savePendingMessage(Coach coachInformation, Message message);
    Mono<Void> deletePendingMessage(Coach coachInformation);
}
