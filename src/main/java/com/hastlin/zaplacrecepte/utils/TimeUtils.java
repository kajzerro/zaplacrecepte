package com.hastlin.zaplacrecepte.utils;


import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    private TimeUtils() {
    }

    public static ZonedDateTime yesterdays23h59m() {
        return yesterdaysMidnight().plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999000);
    }

    public static ZonedDateTime yesterdaysMidnight() {
        ZonedDateTime yesterday = ZonedDateTime.now().minusDays(1);
        return ZonedDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"));
    }
}
