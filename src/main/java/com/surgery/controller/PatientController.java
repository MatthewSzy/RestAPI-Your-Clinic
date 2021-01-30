package com.surgery.controller;

import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.doctor.response.DoctorDetailsForPatientResponse;
import com.surgery.dto.patient.request.CreatePatientRequest;
import com.surgery.dto.patient.request.LogInPatientRequest;
import com.surgery.dto.patient.response.PatientDetailsResponse;
import com.surgery.dto.patient.request.UpdatePatientRequest;
import com.surgery.dto.visit.response.VisitDetailsForPatientResponse;
import com.surgery.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4500"})
@RequestMapping(value = "/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<MessageResponse> createNewPatient(@RequestBody CreatePatientRequest request) throws NoSuchAlgorithmException {

        MessageResponse result = patientService.createNewPatient(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<PatientDetailsResponse> logPatient(@RequestBody LogInPatientRequest request) throws NoSuchAlgorithmException {

        PatientDetailsResponse result = patientService.logPatient(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/get/doctor/{doctorId}")
    public ResponseEntity<DoctorDetailsForPatientResponse> getDoctorBySpecificId(@PathVariable(name = "doctorId") Long doctorId) {

        DoctorDetailsForPatientResponse doctor = patientService.getDoctorBySpecificId(doctorId);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping(path = "/get/doctors")
    public ResponseEntity<List<DoctorDetailsForPatientResponse>> getListOfDoctors() {

        List<DoctorDetailsForPatientResponse> listOfDoctors = patientService.getListOfDoctors();
        return ResponseEntity.ok(listOfDoctors);
    }

    @GetMapping(path = "/get/doctors-spec/{specialization}")
    public ResponseEntity<List<DoctorDetailsForPatientResponse>> getListOfDoctorsWhoHasSpecificSpecialization(@PathVariable(name = "specialization") String specialization) {

        List<DoctorDetailsForPatientResponse> listOfDoctors = patientService.getListOfDoctorsWhoHasSpecificSpecialization(specialization);
        return ResponseEntity.ok(listOfDoctors);
    }

    @GetMapping(path = "/get/doctors-state/{state}")
    public ResponseEntity<List<DoctorDetailsForPatientResponse>> getListOfDoctorsWhoWorkInSpecificState(@PathVariable(name = "state") String state) {

        List<DoctorDetailsForPatientResponse> listOfDoctors = patientService.getListOfDoctorsWhoWorkInSpecificState(state);
        return ResponseEntity.ok(listOfDoctors);
    }

    @GetMapping(path = "/get/doctors-spec-state/")
    public ResponseEntity<List<DoctorDetailsForPatientResponse>> getListOfDoctorsWithDoubleFilters(@RequestParam (name = "state") String state,
                                                                                                   @RequestParam (name = "specialization") String specialization) {

        List<DoctorDetailsForPatientResponse> listOfDoctors = patientService.getListOfDoctorsWithDoubleFilters(state, specialization);
        return ResponseEntity.ok(listOfDoctors);
    }

    @GetMapping(path = "/visit/get-list/{patientId}")
    public ResponseEntity<List<VisitDetailsForPatientResponse>> getListOfVisits(@PathVariable(name = "patientId") Long patientId) {

        List<VisitDetailsForPatientResponse> result = patientService.getListOfVisits(patientId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/visit/get/{visitId}")
    public ResponseEntity<VisitDetailsForPatientResponse> getVisitById(@PathVariable(name = "visitId") Long visitId) {

        VisitDetailsForPatientResponse result = patientService.getVisitById(visitId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(path = "/update/{patientId}")
    public ResponseEntity<MessageResponse> updatePatientData(@PathVariable(name = "patientId") Long patientId,
                                                             @RequestBody UpdatePatientRequest request) {

        MessageResponse result = patientService.updatePatientData(patientId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/delete/{patientId}")
    public ResponseEntity<MessageResponse> deletePatientById(@PathVariable(name = "patientId") Long patientId) {

        MessageResponse result = patientService.deletePatientById(patientId);
        return ResponseEntity.ok(result);
    }
}
