package com.hastlin.zaplacrecepte.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.mail.SendFailedException;

//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class, SendFailedException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        System.out.println("BLA" + ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleArgumentNotVali(RuntimeException ex, WebRequest request) {
        System.out.println("BLA" + ex.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
