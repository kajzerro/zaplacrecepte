package com.hastlin.zaplacrecepte.scheduled;

import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PaymentReminderScheduledTaskTest {

    @SpyBean
    PaymentReminderScheduledTask tasks;

    @Test
    public void reportCurrentTime() {
        await().atMost(new Duration(15, TimeUnit.SECONDS)).untilAsserted(() -> {
            verify(tasks, Mockito.atLeast(1)).reportCurrentTime();
        });
    }
}