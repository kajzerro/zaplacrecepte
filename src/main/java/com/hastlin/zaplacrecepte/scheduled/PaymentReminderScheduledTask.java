package com.hastlin.zaplacrecepte.scheduled;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.service.PrescriptionService;
import com.hastlin.zaplacrecepte.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class PaymentReminderScheduledTask {

	private final static String STATUS_UNPAID = "NEW";

	@Value("${paymentReminder.cronExpression}")
	String cronExpression;

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private PrescriptionService prescriptionService;

	@Scheduled(cron = "${paymentReminder.cronExpression}", zone = "Europe/Warsaw")
	public void reportCurrentTime() {
		List<PrescriptionEntity> unpaidPrescriptionsFromYesterday = prescriptionRepository.findByCreateDateTimeBetweenAndStatusEquals(TimeUtils.yesterdaysMidnight(), TimeUtils.yesterdays23h59m(), STATUS_UNPAID);
		unpaidPrescriptionsFromYesterday.forEach(sendReminder());
	}

	private Consumer<PrescriptionEntity> sendReminder() {
		return unpaidPrescription -> prescriptionService.sendPaymentRequestsViaEmailAndSms(unpaidPrescription);
	}

}