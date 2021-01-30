package com.surgery.controller;

import com.surgery.dto.doctor.request.UpdateDoctorRequest;
import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.doctor.request.CreateDoctorRequest;
import com.surgery.dto.doctor.response.DoctorDetailsResponse;
import com.surgery.dto.doctor.request.LogInDoctorRequest;
import com.surgery.dto.patient.response.PatientDetailsForDoctorResponse;
import com.surgery.dto.visit.request.CreateVisitRequest;
import com.surgery.dto.visit.request.UpdateVisitRequest;
import com.surgery.dto.visit.response.VisitDetailsForDoctorResponse;
import com.surgery.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4500"})
@RequestMapping(value = "/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<MessageResponse> createNewDoctor(@RequestBody CreateDoctorRequest request) throws NoSuchAlgorithmException {

        MessageResponse result = doctorService.createNewDoctor(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<DoctorDetailsResponse> logDoctor(@RequestBody LogInDoctorRequest request) throws NoSuchAlgorithmException {

        DoctorDetailsResponse result = doctorService.logDoctor(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/visit/create")
    public ResponseEntity<MessageResponse> createNewVisit(@RequestBody CreateVisitRequest request) {

        MessageResponse result = doctorService.createNewVisit(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "get/patient/{patientId}")
    public ResponseEntity<PatientDetailsForDoctorResponse> getPatientBySpecificId(@PathVariable (name = "patientId") Long patientId) {

        PatientDetailsForDoctorResponse result = doctorService.getPatientBySpecificId(patientId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/visit/get-list/{doctorId}")
    public ResponseEntity<List<VisitDetailsForDoctorResponse>> getListOfVisits(@PathVariable(name = "doctorId") Long doctorId) {

        List<VisitDetailsForDoctorResponse> result = doctorService.getListOfVisits(doctorId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/visit/get/{visitId}")
    public ResponseEntity<VisitDetailsForDoctorResponse> getVisitById(@PathVariable(name = "visitId") Long visitId) {

        VisitDetailsForDoctorResponse result = doctorService.getVisitById(visitId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/your-patients/{doctorId}")
    public ResponseEntity<List<PatientDetailsForDoctorResponse>> getYourPatients(@PathVariable(name = "doctorId") Long doctorId) {

        List<PatientDetailsForDoctorResponse> result = doctorService.getYourPatients(doctorId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(path = "/update/{doctorId}")
    public ResponseEntity<MessageResponse> updateDoctorData(@PathVariable(name = "doctorId") Long doctorId,
                                                             @RequestBody UpdateDoctorRequest request) {

        MessageResponse result = doctorService.updateDoctorData(doctorId, request);
        return ResponseEntity.ok(result);
    }

    @PutMapping(path = "/visit/update/{visitId}")
    public ResponseEntity<MessageResponse> updateVisitData(@PathVariable(name = "visitId") Long visitId,
                                                           @RequestBody UpdateVisitRequest request) {

        MessageResponse result = doctorService.updateVisitData(visitId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/delete/{doctorId}")
    public ResponseEntity<MessageResponse> deleteDoctorById(@PathVariable(name = "doctorId") Long doctorId) {

        MessageResponse result = doctorService.deleteDoctorById(doctorId);
        return ResponseEntity.ok(result);
    }
}
