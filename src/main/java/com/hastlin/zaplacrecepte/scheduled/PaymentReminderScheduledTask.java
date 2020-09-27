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
import java.util.stream.Collectors;

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
		log.info("Found {} unapaid prescriptions, unpaid ids are: {}", unpaidPrescriptionsFromYesterday.size(), unpaidPrescriptionsFromYesterday.stream().map(pe -> pe.getId()).collect(Collectors.toList()));
		unpaidPrescriptionsFromYesterday.forEach(sendReminder());
		log.info("Ended job to remind about unpaid prescriptions");
	}

	private Consumer<PrescriptionEntity> sendReminder() {
		return unpaidPrescription -> prescriptionService.sendPaymentRequestsViaEmailAndSms(unpaidPrescription);
	}

}