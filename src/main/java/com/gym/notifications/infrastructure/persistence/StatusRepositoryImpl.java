package com.gym.notifications.infrastructure.persistence;

import com.gym.notifications.domain.model.Coach;
import com.gym.notifications.domain.repository.StatusRepository;
import com.gym.notifications.domain.status.CoachStatus;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatusRepositoryImpl implements StatusRepository {
    private final ReactiveHashOperations<String, String, String> reactiveRedisOperations;
    private final ReactiveRedisOperations<String, Object> objectReactiveRedisOperations;

    public StatusRepositoryImpl(ReactiveHashOperations<String, String, String> reactiveRedisOperations, ReactiveRedisOperations<String, Object> objectReactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.objectReactiveRedisOperations = objectReactiveRedisOperations;
    }


    @Override
    public Mono<Void> updateToOffline(Coach coachInformation) {
        String coachKey = "coach:" + coachInformation.coachName();
        String coachOffline = CoachStatus.OFFLINE.name();

        return reactiveRedisOperations.get(coachKey, "status")
                .flatMap(status -> {
                    if (!status.equals(CoachStatus.OFFLINE.name())) {
                        return objectReactiveRedisOperations.opsForValue().set(coachKey, coachOffline);
                    } else {
                        return Mono.error(new Exception("User is already Offline"));
                    }
                }).then();
    }

    @Override
    public Mono<Void> updateToOnline(Coach coachInformation) {
        String coachKey = "coach:" + coachInformation.coachName();
        String coachOnline = CoachStatus.ONLINE.name();

        return reactiveRedisOperations.get(coachKey, "status")
                .flatMap(status -> {
                    if (!status.equals(CoachStatus.ONLINE.name())) {

                        return objectReactiveRedisOperations.opsForValue().set(coachKey, coachOnline);


                    } else {
                        return Mono.error(new Exception("Coach is already online"));
                    }
                }).then();
    }

    @Override
    public Mono<Void> saveCoach(Coach coachInformation) {
        String coachKey = "coach:" + coachInformation.coachName();

        Map<String, String> coachData = new HashMap<>();
        coachData.put("id", String.valueOf(coachInformation.coachId()));
        coachData.put("name", coachInformation.coachName());
        coachData.put("status", CoachStatus.OFFLINE.name());

        return reactiveRedisOperations.
                putAll(coachKey, coachData)
                .then();
    }
}
