package com.smart.restaurantAppointment.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {

    /** Format: yyyy-MM-dd-HH (e.g. 2026-03-18-18) */
    private static final DateTimeFormatter DATE_HOUR_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
    private DateTimeUtils() {}

    /**
     * Format dateTime as yyyy-MM-dd-HH.
     * Use for: Redis keys, logging, compact display, etc.
     */
    public static String formatDateHour(LocalDateTime dateTime) {
        return dateTime.format(DATE_HOUR_FORMAT);
    }

}
