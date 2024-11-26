package com.gym.notifications.domain.factories;

import com.gym.notifications.domain.model.Coach;

public interface AdminFactory {

    Coach createCoach(byte[] message);
}
