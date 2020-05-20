package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.dto.PrescriptionDTO;
import com.hastlin.zaplacrecepte.model.mapper.PrescriptionMapper;
import com.hastlin.zaplacrecepte.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by mateuszkaszyk on 15/05/2020.
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @PostMapping
    ResponseEntity createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        System.out.print("Got a request " + prescriptionDTO);
        this.prescriptionService.createNewPrescription(prescriptionMapper.toEntity(prescriptionDTO));
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    List<PrescriptionDTO> getAllPrescriptions() {
        return StreamSupport.stream(this.prescriptionService.getAllPrescriptions().spliterator(), false).map(this.prescriptionMapper::toDto).collect(Collectors.toList());
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity patchPrescription(@PathVariable String id, @RequestBody PrescriptionDTO prescriptionDTO) {
        this.prescriptionService.patchPrescription(id, this.prescriptionMapper.toEntity(prescriptionDTO));
        return ResponseEntity.ok("OK");
    }

}
