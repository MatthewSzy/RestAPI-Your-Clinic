package com.surgery.service;

import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.reservation.request.CreateReservationRequest;
import com.surgery.dto.reservation.request.UpdateLinkRequest;
import com.surgery.dto.reservation.response.ReservationDetailsForDoctorResponse;
import com.surgery.dto.reservation.response.ReservationDetailsForPatientResponse;
import com.surgery.dto.reservation.request.ReservationTermRequest;
import com.surgery.entity.Doctor;
import com.surgery.entity.Patient;
import com.surgery.entity.Reservation;
import com.surgery.exception.empty.EmptyFieldException;
import com.surgery.exception.empty.EmptyListException;
import com.surgery.exception.invalid.InvalidFieldException;
import com.surgery.exception.reservation.ReservationNotFoundException;
import com.surgery.exception.user.UserNotFoundException;
import com.surgery.exception.wrong.WrongPatientException;
import com.surgery.generator.HoursGenerator;
import com.surgery.repository.DoctorRepository;
import com.surgery.repository.PatientRepository;
import com.surgery.repository.ReservationRepository;
import com.surgery.validator.ReservationValidator;
import com.surgery.validator.fields.ReservationDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final ReservationRepository reservationRepository;

    private final ReservationValidator reservationValidator;

    @Autowired
    public ReservationService(PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ReservationRepository reservationRepository,
                              ReservationValidator reservationValidator) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.reservationRepository = reservationRepository;
        this.reservationValidator = reservationValidator;
    }

    public MessageResponse createNewReservation(CreateReservationRequest request) {

        Optional<Patient> patient = patientRepository.findById(request.getPatientId());

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID = " + request.getPatientId() + " nie istnieje!");
        }

        Optional<Doctor> doctor = doctorRepository.findById(request.getDoctorId());

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Doktor o ID = " + request.getDoctorId() + " nie istnieje!");
        }

        reservationValidator.validateReservationData(request);

        ReservationTermRequest reservationTermRequest = ReservationTermRequest.builder()
                .reservationDate(request.getReservationDate())
                .reservationTime(request.getReservationTime())
                .doctorId(request.getDoctorId())
                .build();

        reservationValidator.checkIfTermIsFree(reservationTermRequest, reservationRepository);

        Reservation reservation = Reservation.builder()
                .reservationDate(ReservationDateValidator.getDateFromString(request.getReservationDate()))
                .reservationTime(request.getReservationTime())
                .reservationType(request.getReservationType().toString())
                .doctorId(request.getDoctorId())
                .doctorFirstName(doctor.get().getFirstName())
                .doctorLastName(doctor.get().getLastName())
                .patientId(request.getPatientId())
                .patientFirstName(patient.get().getFirstName())
                .patientLastName(patient.get().getLastName())
                .meetingLink("")
                .build();

        reservationRepository.save(reservation);

        return new MessageResponse("Pomyślnie dokonano rezerwacji!");
    }

    public ReservationDetailsForDoctorResponse getReservationForDoctor(Long reservationId) {

        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if(!reservation.isPresent()) {
            throw new ReservationNotFoundException("Rezerwacja nie istnieje!");
        }

        return ReservationDetailsForDoctorResponse.builder()
                .reservationId(reservation.get().getReservationId())
                .reservationDate(reservation.get().getReservationDate())
                .reservationTime(reservation.get().getReservationTime())
                .reservationType(reservation.get().getReservationType())
                .patientId(reservation.get().getPatientId())
                .firstName(reservation.get().getPatientFirstName())
                .lastName(reservation.get().getPatientLastName())
                .meetingLink(reservation.get().getMeetingLink())
                .build();
    }

    public List<ReservationDetailsForDoctorResponse> getListOfReservationsForDoctor(Long doctorId, String reservationDate) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Doktor o ID = " + doctorId + " nie istnieje!");
        }

        if (ReservationDateValidator.checkIfStringHasIncorrectDate(reservationDate)) {
            throw new InvalidFieldException("Podana data jest nieprawidłowa!");
        }

        Date date = ReservationDateValidator.getDateFromString(reservationDate);

        List<Reservation> reservations = reservationRepository.getDoctorReservationsByDate(doctorId, date);

        if (reservations.isEmpty()) {
            throw new EmptyListException("Lista rezerwacji jest pusta!");
        }

        return reservations.stream().map(r -> ReservationDetailsForDoctorResponse.builder()
                .reservationId(r.getReservationId())
                .reservationDate(r.getReservationDate())
                .reservationTime(r.getReservationTime())
                .reservationType(r.getReservationType())
                .patientId(r.getPatientId())
                .firstName(r.getPatientFirstName())
                .lastName(r.getPatientLastName())
                .meetingLink(r.getMeetingLink())
                .build()).collect(Collectors.toList());
    }

    public List<ReservationDetailsForPatientResponse> getListOfReservationsForPatient(Long patientId) {

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID = " + patientId + " nie istnieje!");
        }

        List<Reservation> reservations = reservationRepository.getReservationsByPatientId(patientId);

        if (reservations.isEmpty()) {
            throw new EmptyListException("Lista rezerwacji jest pusta!");
        }

        return reservations.stream().map(r -> ReservationDetailsForPatientResponse.builder()
                .reservationId(r.getReservationId())
                .reservationDate(r.getReservationDate())
                .reservationTime(r.getReservationTime())
                .reservationType(r.getReservationType())
                .doctorId(r.getDoctorId())
                .firstName(r.getDoctorFirstName())
                .lastName(r.getDoctorLastName())
                .meetingLink(r.getMeetingLink())
                .build()).collect(Collectors.toList());
    }

    public List<String> getListOfAvailableHoursForSpecificDay(String reservationDate) {

        if (ReservationDateValidator.checkIfStringHasIncorrectDate(reservationDate)) {
            throw new InvalidFieldException("Podana data jest nieprawidłowa!");
        }

        Date data = ReservationDateValidator.getDateFromString(reservationDate);

        List<Reservation> reservations = reservationRepository.getReservationsByDate(data);

        if (reservations.isEmpty()) {
            return HoursGenerator.generateAllAvailableHoursList();
        }

        List<String> hours = reservations.stream().map(Reservation::getReservationTime).collect(Collectors.toList());

        return HoursGenerator.getListOfAvailableHours(hours);
    }

    public MessageResponse cancelReservation(Long reservationId, Long patientId) {

        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (!reservation.isPresent()) {
            throw new ReservationNotFoundException("Rezerwacja nie istnieje!");
        }

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID = " + patientId + " nie istnieje!");
        }

        Reservation reservationToBeDeleted = reservation.get();

        if (!reservationToBeDeleted.getPatientId().equals(patientId)) {
            throw new WrongPatientException("Pacjent nie ma uprawnień do usunięcia rezerwacji!");
        }

        reservationRepository.delete(reservationToBeDeleted);

        return new MessageResponse("Pomyślnie usunięto rezerwacje!");
    }

    public MessageResponse updateMeetingLink(UpdateLinkRequest request) {

        Optional<Reservation> reservationToBeChecked = reservationRepository.findById(request.getReservationId());

        if (!reservationToBeChecked.isPresent()) {
            throw new ReservationNotFoundException("Rezerwacja nie istnieje!");
        }

        if (request.getMeetingLink().equals("")) {
            throw new EmptyFieldException("Podane pole z linkiem jest puste!");
        }

        Reservation reservation = reservationToBeChecked.get();

        reservation.setMeetingLink(request.getMeetingLink());

        reservationRepository.save(reservation);

        return new MessageResponse("Pomyślnie dodano link!");
    }
}
