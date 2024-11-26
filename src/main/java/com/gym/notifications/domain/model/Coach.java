package com.gym.notifications.domain.model;

import com.gym.notifications.domain.status.CoachStatus;

import java.io.Serializable;

public record Coach(String coachId, String coachName, CoachStatus status) implements Serializable {
}
