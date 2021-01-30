package com.surgery.validator;

import com.surgery.dto.reservation.request.CreateReservationRequest;
import com.surgery.dto.reservation.request.ReservationTermRequest;
import com.surgery.entity.Reservation;
import com.surgery.exception.empty.EmptyFieldException;
import com.surgery.exception.invalid.InvalidFieldException;
import com.surgery.exception.reservation.TermAlreadyTakenException;
import com.surgery.generator.HoursGenerator;
import com.surgery.repository.ReservationRepository;
import com.surgery.validator.fields.ReservationDateValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationValidator {

    private ReservationValidator(){}

    public void validateReservationData(CreateReservationRequest request) {

        if (request.getReservationDate() == null ||
            request.getReservationTime() == null ||
            request.getDoctorId() == null ||
            request.getPatientId() == null) {

            throw new EmptyFieldException("Jedno z pól jest puste! Proszę o ponowne wprowadzenie danych!");
        }

        if (ReservationDateValidator.checkIfStringHasIncorrectDate(request.getReservationDate())) {
            throw new InvalidFieldException("Podana data jest nieprawidłowa!");
        }

        List<String> hoursList = HoursGenerator.generateAllAvailableHoursList();

        if (!hoursList.contains(request.getReservationTime())) {
            throw new InvalidFieldException("Podana godzina jest niepoprawna!");
        }
    }

    public void checkIfTermIsFree(ReservationTermRequest request,
                                         ReservationRepository reservationRepository) {

        List<Reservation> list = reservationRepository.getReservationsByDoctorId(request.getDoctorId());

        for (Reservation reservation : list) {
            if (reservation.getReservationDate().toString().equals(request.getReservationDate()) &&
                reservation.getReservationTime().equals(request.getReservationTime())) {

                throw new TermAlreadyTakenException("Termin jest już zajęty!");
            }
        }
    }
}
