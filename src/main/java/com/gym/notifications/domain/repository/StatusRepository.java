package com.gym.notifications.domain.repository;

import com.gym.notifications.domain.model.Coach;
import reactor.core.publisher.Mono;

public interface StatusRepository {

    Mono<Void> updateToOffline (Coach coachInformation);
    Mono<Void> updateToOnline (Coach coachInformation);
    Mono<Void> saveCoach ( Coach coachInformation);
}
