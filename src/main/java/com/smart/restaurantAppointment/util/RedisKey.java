package com.smart.restaurantAppointment.util;

import java.time.Duration;

public final class RedisKey {
    public static final String BOOKING_LOCKED = "booking:lock";
    public static final String BOOKING_BOOKED = "booking:booked";
    public static final int BOOKING_LOCK_TTL_SECONDS = 10;
    public static final Duration BOOKING_BOOKED_TTL = Duration.ofDays(7);

    public static String bookingLock(Long userId, Long restaurantId, String dateHour) {
        return BOOKING_LOCKED + userId + ":" + restaurantId + ":" + dateHour;
    }
    public static String bookingBooked(Long userId, Long restaurantId, String dateHour) {
        return BOOKING_BOOKED + userId + ":" + restaurantId + ":" + dateHour;
    }

}
