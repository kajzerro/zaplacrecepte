package com.hastlin.zaplacrecepte.utils;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;


public class TimeUtilsTest {

    @Test
    public void should_return_yesterdays_date() {
        assertEquals(ZonedDateTime.now().minusDays(1).getDayOfMonth(), TimeUtils.yesterdays23h59m().getDayOfMonth());
        assertEquals(ZonedDateTime.now().minusDays(1).getDayOfMonth(), TimeUtils.yesterdaysMidnight().getDayOfMonth());

        assertEquals(ZonedDateTime.now().getMonthValue(), TimeUtils.yesterdaysMidnight().getMonthValue());
        assertEquals(ZonedDateTime.now().getMonthValue(), TimeUtils.yesterdays23h59m().getMonthValue());

        assertEquals(ZonedDateTime.now().getYear(), TimeUtils.yesterdaysMidnight().getYear());
        assertEquals(ZonedDateTime.now().getYear(), TimeUtils.yesterdays23h59m().getYear());
    }

    @Test
    public void should_give_whole_day_range_between_methods() {
        assertEquals(TimeUtils.yesterdaysMidnight().getDayOfMonth(), TimeUtils.yesterdays23h59m().getDayOfMonth());
        assertEquals(TimeUtils.yesterdaysMidnight().getMonthValue(), TimeUtils.yesterdays23h59m().getMonthValue());
        assertEquals(TimeUtils.yesterdaysMidnight().getYear(), TimeUtils.yesterdays23h59m().getYear());
        assertEquals(23, TimeUtils.yesterdays23h59m().getHour() - TimeUtils.yesterdaysMidnight().getHour());
        assertEquals(59, TimeUtils.yesterdays23h59m().getMinute() - TimeUtils.yesterdaysMidnight().getMinute());
        assertEquals(59, TimeUtils.yesterdays23h59m().getSecond() - TimeUtils.yesterdaysMidnight().getSecond());
        assertEquals(999999000, TimeUtils.yesterdays23h59m().getNano() - TimeUtils.yesterdaysMidnight().getNano());
    }

}