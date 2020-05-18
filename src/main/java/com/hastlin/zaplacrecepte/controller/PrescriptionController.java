package com.hastlin.zaplacrecepte.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mateuszkaszyk on 15/05/2020.
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @GetMapping("/")
    ResponseEntity createPrescription(){
        return ResponseEntity.ok("[\n" +
                "  {\"name\": \"Piotrx\", \"surname\": \"Krzystyniak\", \"status\": \"paid\"},\n" +
                "  {\"name\": \"Matuś\", \"surname\": \"Kajzer\", \"status\": \"unpaid\"},\n" +
                "  {\"name\": \"Kubuś\", \"surname\": \"Osika\", \"status\": \"done\"}\n" +
                "]");
    }

}
