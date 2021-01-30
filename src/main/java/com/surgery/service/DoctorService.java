package com.surgery.service;

import com.surgery.dto.doctor.request.UpdateDoctorRequest;
import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.doctor.request.CreateDoctorRequest;
import com.surgery.dto.doctor.response.DoctorDetailsResponse;
import com.surgery.dto.doctor.request.LogInDoctorRequest;
import com.surgery.dto.patient.response.PatientDetailsForDoctorResponse;
import com.surgery.dto.visit.request.CreateVisitRequest;
import com.surgery.dto.visit.request.UpdateVisitRequest;
import com.surgery.dto.visit.response.VisitDetailsForDoctorResponse;
import com.surgery.entity.Doctor;
import com.surgery.entity.Patient;
import com.surgery.entity.Reservation;
import com.surgery.entity.Visit;
import com.surgery.exception.incomplete.IncompleteRequestException;
import com.surgery.exception.invalid.InvalidFieldException;
import com.surgery.exception.reservation.ReservationNotFoundException;
import com.surgery.exception.user.UserNotFoundException;
import com.surgery.exception.visit.VisitNotFoundException;
import com.surgery.hash.PasswordHash;
import com.surgery.repository.DoctorRepository;
import com.surgery.repository.PatientRepository;
import com.surgery.repository.ReservationRepository;
import com.surgery.repository.VisitRepository;
import com.surgery.validator.RequestDataValidator;
import com.surgery.validator.UserExistenceValidator;
import com.surgery.validator.fields.DateOfBirthValidator;
import com.surgery.validator.fields.EmailValidator;
import com.surgery.validator.fields.GenderValidator;
import com.surgery.validator.fields.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final ReservationRepository reservationRepository;

    private final VisitRepository visitRepository;

    private final RequestDataValidator requestDataValidator;

    private final UserExistenceValidator userExistenceValidator;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         PatientRepository patientRepository,
                         ReservationRepository reservationRepository,
                         VisitRepository visitRepository,
                         RequestDataValidator requestDataValidator,
                         UserExistenceValidator userExistenceValidator) {

        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.reservationRepository = reservationRepository;
        this.visitRepository = visitRepository;
        this.requestDataValidator = requestDataValidator;
        this.userExistenceValidator = userExistenceValidator;
    }

    public MessageResponse createNewDoctor(CreateDoctorRequest request) throws NoSuchAlgorithmException {

        String pesel = request.getPesel();
        String email = request.getEmail();

        userExistenceValidator.checkIfUserWithGivenEmailAlreadyExist(request.getEmail(), patientRepository, doctorRepository);
        userExistenceValidator.checkIfUserWithGivenPeselAlreadyExist(request.getPesel(), patientRepository, doctorRepository);

        requestDataValidator.validateDoctorRegisterData(request);

        String hashedPassword = PasswordHash.hashPassword(request.getPassword());

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .pwz(request.getPwz())
                .specialization(request.getSpecialization())
                .city(request.getCity())
                .street(request.getStreet())
                .state(request.getState())
                .pesel(pesel)
                .dateBirth(DateOfBirthValidator.getDateFromPesel(pesel))
                .gender(GenderValidator.getGender(request.getPesel()))
                .phone(request.getPhone())
                .email(email)
                .password(hashedPassword)
                .build();

        doctorRepository.save(doctor);

        String newPassword = String.format("%04d%s", doctor.getDoctorID(), doctor.getPassword());
        doctor.setPassword(newPassword);

        doctorRepository.save(doctor);

        return new MessageResponse("Rejestracja przebiegła pomyślnie!");
    }

    public DoctorDetailsResponse logDoctor(LogInDoctorRequest request) throws NoSuchAlgorithmException {

        Doctor doctor = doctorRepository.getDoctorByEmail(request.getEmail());

        if (doctor == null) {
            throw new UserNotFoundException("Użytkownik o e-mail'u: " + request.getEmail() + " nie istnieje!");
        }

        String password = doctorRepository.getPasswordByEmail(request.getEmail());
        String hashedPasswordToBeChecked = PasswordHash.hashPassword(request.getPassword());

        hashedPasswordToBeChecked = String.format("%04d%s", doctor.getDoctorID(), hashedPasswordToBeChecked);

        if (!password.equals(hashedPasswordToBeChecked)) {
            throw new InvalidFieldException("Podane hasło jest nieprawidłowe!");
        }

        return DoctorDetailsResponse.builder()
                .doctorId(doctor.getDoctorID())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .pwz(doctor.getPwz())
                .specialization(doctor.getSpecialization())
                .city(doctor.getCity())
                .street(doctor.getStreet())
                .state(doctor.getState())
                .pesel(doctor.getPesel())
                .dateOfBirth(doctor.getDateBirth())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build();
    }

    public PatientDetailsForDoctorResponse getPatientBySpecificId(Long patientId) {

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID: " + patientId + " nie istnieje!");
        }

        return PatientDetailsForDoctorResponse.builder()
                .firstName(patient.get().getFirstName())
                .lastName(patient.get().getLastName())
                .city(patient.get().getCity())
                .street(patient.get().getStreet())
                .state(patient.get().getState())
                .pesel(patient.get().getPesel())
                .dateOfBirth(patient.get().getDateBirth())
                .phone(patient.get().getPhone())
                .email(patient.get().getEmail())
                .build();
    }

    public MessageResponse updateDoctorData(Long doctorId, UpdateDoctorRequest request) {

        if (requestDataValidator.validateUpdateRequests(request, null)) {
            throw new IncompleteRequestException("Żądanie jest niekompletne!");
        }

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Użytkownik o ID: " + doctorId + " nie istnieje!");
        }

        Doctor doctorToUpdate = doctor.get();

        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String city = request.getCity();
        String street = request.getStreet();
        String state = request.getState();
        String phone = request.getPhone();
        String email = request.getEmail();

        if (!firstName.equals("") && firstName.length() >= 3) {
            doctorToUpdate.setFirstName(firstName);
        }

        if (!lastName.equals("") && lastName.length() >= 2) {
            doctorToUpdate.setLastName(lastName);
        }

        if (!city.equals("")) {
            doctorToUpdate.setCity(city);
        }

        if (!street.equals("")) {
            doctorToUpdate.setStreet(street);
        }

        if (!state.equals("")) {
            doctorToUpdate.setState(state);
        }

        if (!phone.equals("") && PhoneNumberValidator.checkIfPhoneNumberIsInvalid(phone)) {
            throw new InvalidFieldException("Podany numer telefonu jest niepoprawny!");
        }

        if (!phone.equals("")) {
            doctorToUpdate.setPhone(phone);
        }

        if (!email.equals("") && EmailValidator.checkIfEmailIsInvalid(email)) {
            throw new InvalidFieldException("Podany email jest niepoprawny!");
        }

        if (!email.equals(doctorToUpdate.getEmail())) {
            userExistenceValidator.checkIfUserWithGivenEmailAlreadyExist(email, patientRepository, doctorRepository);
        }

        if (!email.equals("")) {
            doctorToUpdate.setEmail(email);
        }

        doctorRepository.save(doctorToUpdate);

        return new MessageResponse("Pomyślnie zaktualizowano użytkownika!");
    }

    public MessageResponse deleteDoctorById(Long doctorId) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Użytkownik o ID = " + doctorId + " nie istnieje!");
        }

        doctorRepository.delete(doctor.get());

        return new MessageResponse("Pomyślnie usunięto użytkownika!");
    }

    public MessageResponse createNewVisit(CreateVisitRequest request) {

        Optional<Reservation> reservationToBeChecked = reservationRepository.findById(request.getReservationId());

        if (!reservationToBeChecked.isPresent()) {
            throw new ReservationNotFoundException("Rezerwacja o ID = " + request.getReservationId() + " nie istnieje!");
        }

        Reservation reservation = reservationToBeChecked.get();

        Optional<Doctor> doctorToBeChecked = doctorRepository.findById(reservation.getDoctorId());

        if (!doctorToBeChecked.isPresent()) {
            throw new UserNotFoundException("Doktor o ID = " + reservation.getDoctorId() + " nie istnieje!");
        }

        Optional<Patient> patientToBeChecked = patientRepository.findById(reservation.getPatientId());

        if (!patientToBeChecked.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID = " + reservation.getPatientId() + " nie istnieje!");
        }

        Visit visit = Visit.builder()
                .visitDate(reservation.getReservationDate())
                .visitTime(reservation.getReservationTime())
                .visitType(reservation.getReservationType())
                .doctorId(reservation.getDoctorId())
                .doctorFirstName(reservation.getDoctorFirstName())
                .doctorLastName(reservation.getDoctorLastName())
                .patientId(reservation.getPatientId())
                .patientFirstName(reservation.getPatientFirstName())
                .patientLastName(reservation.getPatientLastName())
                .symptoms(request.getSymptoms())
                .diagnosis(request.getDiagnosis())
                .prescribedMedications(request.getPrescribedMedications())
                .takingMedications(request.getTakingMedications())
                .recommendations(request.getRecommendations())
                .build();

        visitRepository.save(visit);

        reservationRepository.delete(reservation);

        return new MessageResponse("Pomyślnie utworzono nową wizytę!");
    }

    public MessageResponse updateVisitData(Long visitId, UpdateVisitRequest request) {

        Optional<Visit> visit = visitRepository.findById(visitId);

        if(!visit.isPresent()) {
            throw new VisitNotFoundException("Wizyta o ID = " + visitId + " nie istnieje!");
        }

        Visit visitToUpdate = visit.get();

        String symptoms = request.getSymptoms();
        String diagnosis = request.getDiagnosis();
        String prescribedMedications = request.getPrescribedMedications();
        String takingMedications = request.getTakingMedications();
        String recommendations = request.getRecommendations();

        if(!symptoms.equals("")) {
            visitToUpdate.setSymptoms(symptoms);
        }

        if(!diagnosis.equals("")) {
            visitToUpdate.setDiagnosis(diagnosis);
        }

        if(!prescribedMedications.equals("")) {
            visitToUpdate.setPrescribedMedications(prescribedMedications);
        }

        if(!takingMedications.equals("")) {
            visitToUpdate.setTakingMedications(takingMedications);
        }

        if(!recommendations.equals("")) {
            visitToUpdate.setRecommendations(recommendations);
        }

        visitRepository.save(visitToUpdate);

        return new MessageResponse("Pomyślnie zmodyfikowano informacje w wizycie!");
    }

    public List<VisitDetailsForDoctorResponse> getListOfVisits(Long doctorId) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Doktor o ID = " + doctorId + " nie istnieje!");
        }

        List<Visit> visitList = visitRepository.getVisitsByDoctorId(doctorId);

        return visitList.stream().map(v -> VisitDetailsForDoctorResponse.builder()
                .visitId(v.getVisitId())
                .visitDate(v.getVisitDate().toString())
                .visitTime(v.getVisitTime())
                .visitType(v.getVisitType())
                .patientId(v.getPatientId())
                .patientFirstName(v.getPatientFirstName())
                .patientLastName(v.getPatientLastName())
                .symptoms(v.getSymptoms())
                .diagnosis(v.getDiagnosis())
                .prescribedMedications(v.getPrescribedMedications())
                .takingMedications(v.getTakingMedications())
                .recommendations(v.getRecommendations())
                .build()).collect(Collectors.toList());
    }

    public VisitDetailsForDoctorResponse getVisitById(Long visitId) {

        Optional<Visit> visitToBeChecked = visitRepository.findById(visitId);

        if (!visitToBeChecked.isPresent()) {
            throw new VisitNotFoundException("Wizyta o ID = " + visitId + " nie istnieje!");
        }

        Visit visit = visitToBeChecked.get();

        return VisitDetailsForDoctorResponse.builder()
                .visitId(visitId)
                .visitDate(visit.getVisitDate().toString())
                .visitTime(visit.getVisitTime())
                .visitType(visit.getVisitType())
                .patientId(visit.getPatientId())
                .patientFirstName(visit.getPatientFirstName())
                .patientLastName(visit.getPatientLastName())
                .symptoms(visit.getSymptoms())
                .diagnosis(visit.getDiagnosis())
                .prescribedMedications(visit.getPrescribedMedications())
                .takingMedications(visit.getTakingMedications())
                .recommendations(visit.getRecommendations())
                .build();
    }

    public List<PatientDetailsForDoctorResponse> getYourPatients(Long doctorId) {

        Optional<Doctor> doctorToBeChecked = doctorRepository.findById(doctorId);

        if (!doctorToBeChecked.isPresent()) {
            throw new UserNotFoundException("Doktor o ID = " + doctorId + " nie istnieje!");
        }

        List<Visit> visitList = visitRepository.getVisitsByDoctorId(doctorId);

        List<Long> idList = new ArrayList<>();

        for (Visit visit : visitList) {
            if (!idList.contains(visit.getPatientId())) {
                idList.add(visit.getPatientId());
            }
        }

        List<Patient> patientList = new ArrayList<>();

        for (Long id : idList) {
            Optional<Patient> patient = patientRepository.findById(id);

            if (!patient.isPresent()) {
                throw new UserNotFoundException("Pacjent o ID = " + id + " nie istnieje!");
            }

            patientList.add(patient.get());
        }

        return patientList.stream().map(p -> PatientDetailsForDoctorResponse.builder()
                .patientId(p.getPatientId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .city(p.getCity())
                .street(p.getStreet())
                .state(p.getState())
                .pesel(p.getPesel())
                .dateOfBirth(p.getDateBirth())
                .phone(p.getPhone())
                .email(p.getEmail())
                .build()).collect(Collectors.toList());
    }
}
