package com.surgery.controller;

import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.reservation.request.CreateReservationRequest;
import com.surgery.dto.reservation.request.UpdateLinkRequest;
import com.surgery.dto.reservation.response.ReservationDetailsForDoctorResponse;
import com.surgery.dto.reservation.response.ReservationDetailsForPatientResponse;
import com.surgery.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4500"})
@RequestMapping(value = "/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<MessageResponse> createNewReservation(@RequestBody CreateReservationRequest request) {

        MessageResponse result = reservationService.createNewReservation(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/get/{reservationId}")
    public ResponseEntity<ReservationDetailsForDoctorResponse> getReservationForDoctor(@PathVariable(name = "reservationId") Long reservationId) {

        ReservationDetailsForDoctorResponse result = reservationService.getReservationForDoctor(reservationId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/get-list/doctor")
    public ResponseEntity<List<ReservationDetailsForDoctorResponse>> getListOfReservationsForDoctor(@RequestParam(name = "doctorId") Long doctorId,
                                                                                                    @RequestParam(name = "reservationDate") String reservationDate) {

        List<ReservationDetailsForDoctorResponse> result = reservationService.getListOfReservationsForDoctor(doctorId, reservationDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/get-list/patient/{patientId}")
    public ResponseEntity<List<ReservationDetailsForPatientResponse>> getListOfReservationsForPatient(@PathVariable(name = "patientId") Long patientId) {

        List<ReservationDetailsForPatientResponse> result = reservationService.getListOfReservationsForPatient(patientId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/get-list/hours/{reservationDate}")
    public ResponseEntity<List<String>> getListOfAvailableHoursForSpecificDay(@PathVariable(name = "reservationDate") String reservationDate) {

        List<String> result = reservationService.getListOfAvailableHoursForSpecificDay(reservationDate);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(path = "/update-link")
    public ResponseEntity<MessageResponse> updateMeetingLink(@RequestBody UpdateLinkRequest request) {

        MessageResponse result = reservationService.updateMeetingLink(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/cancel")
    public ResponseEntity<MessageResponse> cancelReservation(@RequestParam(name = "reservationId") Long reservationId,
                                                    @RequestParam(name = "patientId") Long patientId) {

        MessageResponse result = reservationService.cancelReservation(reservationId, patientId);
        return ResponseEntity.ok(result);
    }
}
