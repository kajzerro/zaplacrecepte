package com.hastlin.zaplacrecepte.service.exception;

/**
 * Created by mateuszkaszyk on 02/06/2020.
 */
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
