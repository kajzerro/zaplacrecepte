package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.PrescriptionDTO;
import com.hastlin.zaplacrecepte.model.mapper.PrescriptionMapper;
import com.hastlin.zaplacrecepte.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mateuszkaszyk on 15/05/2020.
 */
@RestController
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @PostMapping("/prescription")
    ResponseEntity createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        System.out.print("Got a request " + prescriptionDTO);
        this.prescriptionService.createNewPrescription(prescriptionMapper.toEntity(prescriptionDTO));
        return ResponseEntity.ok("OK");
    }


}
