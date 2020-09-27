package com.hastlin.zaplacrecepte.utils;


import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    public static ZonedDateTime yesterdays23h59m() {
        return yesterdaysMidnight().plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999999999);
    }

    public static ZonedDateTime yesterdaysMidnight() {
        return ZonedDateTime.of(ZonedDateTime.now().getYear(), ZonedDateTime.now().getMonthValue(), ZonedDateTime.now().minusDays(1).getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("Europe/Warsaw"));
    }
}
