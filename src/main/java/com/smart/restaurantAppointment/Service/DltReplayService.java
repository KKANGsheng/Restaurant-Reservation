package com.smart.restaurantAppointment.Service;

public interface DltReplayService {
    int replayDeadLetterMessage(String dltTopic, int maxMessage);
}
