package com.hastlin.zaplacrecepte.scheduled;

import com.hastlin.zaplacrecepte.model.entity.PrescriptionEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.service.PrescriptionService;
import com.hastlin.zaplacrecepte.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
@Slf4j
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
		log.info("Starting job to remind about unpaid prescriptions");
		List<PrescriptionEntity> unpaidPrescriptionsFromYesterday = prescriptionRepository.findByCreateDateTimeBetweenAndStatusEquals(TimeUtils.yesterdaysMidnight(), TimeUtils.yesterdays23h59m(), STATUS_UNPAID);
		unpaidPrescriptionsFromYesterday.forEach(sendReminder());
		log.info("Ended job to remind about unpaid prescriptions, found {} unapaid prescriptions", unpaidPrescriptionsFromYesterday.size());
	}

	private Consumer<PrescriptionEntity> sendReminder() {
		return unpaidPrescription -> prescriptionService.sendPaymentRequestsViaEmailAndSms(unpaidPrescription);
	}

}