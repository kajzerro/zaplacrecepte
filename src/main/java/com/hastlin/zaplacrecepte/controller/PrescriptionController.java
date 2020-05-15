package com.hastlin.zaplacrecepte.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mateuszkaszyk on 15/05/2020.
 */
@RestController
public class PrescriptionController {

    @GetMapping("/prescription")
    ResponseEntity createPrescription(){
        return ResponseEntity.ok("BLABLA");
    }

}
