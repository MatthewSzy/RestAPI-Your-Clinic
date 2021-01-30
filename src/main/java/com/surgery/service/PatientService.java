package com.surgery.service;

import com.surgery.dto.message.MessageResponse;
import com.surgery.dto.doctor.response.DoctorDetailsForPatientResponse;
import com.surgery.dto.patient.request.CreatePatientRequest;
import com.surgery.dto.patient.request.LogInPatientRequest;
import com.surgery.dto.patient.response.PatientDetailsResponse;
import com.surgery.dto.patient.request.UpdatePatientRequest;
import com.surgery.dto.visit.response.VisitDetailsForDoctorResponse;
import com.surgery.dto.visit.response.VisitDetailsForPatientResponse;
import com.surgery.entity.Doctor;
import com.surgery.entity.Patient;
import com.surgery.entity.Visit;
import com.surgery.exception.empty.EmptyListException;
import com.surgery.exception.incomplete.IncompleteRequestException;
import com.surgery.exception.invalid.InvalidFieldException;
import com.surgery.exception.user.UserNotFoundException;
import com.surgery.exception.visit.VisitNotFoundException;
import com.surgery.hash.PasswordHash;
import com.surgery.repository.DoctorRepository;
import com.surgery.repository.PatientRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final VisitRepository visitRepository;

    private final RequestDataValidator requestDataValidator;

    private final UserExistenceValidator userExistenceValidator;

    @Autowired
    public PatientService(PatientRepository patientRepository,
                          DoctorRepository doctorRepository,
                          VisitRepository visitRepository,
                          RequestDataValidator requestDataValidator,
                          UserExistenceValidator userExistenceValidator) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.visitRepository = visitRepository;
        this.requestDataValidator = requestDataValidator;
        this.userExistenceValidator = userExistenceValidator;
    }

    public MessageResponse createNewPatient(CreatePatientRequest request) throws NoSuchAlgorithmException {

        String email = request.getEmail();
        String pesel = request.getPesel();

        userExistenceValidator.checkIfUserWithGivenEmailAlreadyExist(email, patientRepository, doctorRepository);
        userExistenceValidator.checkIfUserWithGivenPeselAlreadyExist(pesel, patientRepository, doctorRepository);

        requestDataValidator.validatePatientRegisterData(request);

        String hashedPassword = PasswordHash.hashPassword(request.getPassword());

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
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

        patientRepository.save(patient);

        String newPassword = String.format("%04d%s", patient.getPatientId(), patient.getPassword());
        patient.setPassword(newPassword);

        patientRepository.save(patient);

        return new MessageResponse("Rejestracja przebiegła pomyślnie!");
    }

    public PatientDetailsResponse logPatient(LogInPatientRequest request) throws NoSuchAlgorithmException {

        Patient patient = patientRepository.getPatientByEmail(request.getEmail());

        if (patient == null) {
            throw new UserNotFoundException("Użytkownik o e-mail'u " + request.getEmail() + " nie istnieje!");
        }

        String password = patientRepository.getPasswordByEmail(request.getEmail());
        String hashedPasswordToBeChecked = PasswordHash.hashPassword(request.getPassword());

        hashedPasswordToBeChecked = String.format("%04d%s", patient.getPatientId(), hashedPasswordToBeChecked);

        if (!password.equals(hashedPasswordToBeChecked)) {
            throw new InvalidFieldException("Podane hasło jest nieprawidłowe!");
        }

        return PatientDetailsResponse.builder()
                .patientID(patient.getPatientId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .city(patient.getCity())
                .street(patient.getStreet())
                .state(patient.getState())
                .pesel(patient.getPesel())
                .dateOfBirth(patient.getDateBirth())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .build();
    }

    public DoctorDetailsForPatientResponse getDoctorBySpecificId(Long doctorId) {

        Optional<Doctor> doctor = doctorRepository.findById(doctorId);

        if (!doctor.isPresent()) {
            throw new UserNotFoundException("Doktor o ID: " + doctorId + " nie istnieje!");
        }

        return DoctorDetailsForPatientResponse.builder()
                .doctorID((doctor.get().getDoctorID()))
                .firstName(doctor.get().getFirstName())
                .lastName(doctor.get().getLastName())
                .pwz(doctor.get().getPwz())
                .specialization(doctor.get().getSpecialization())
                .dateOfBirth(doctor.get().getDateBirth())
                .city(doctor.get().getCity())
                .street(doctor.get().getStreet())
                .state(doctor.get().getState())
                .phone(doctor.get().getPhone())
                .email(doctor.get().getEmail())
                .build();
    }

    public List<DoctorDetailsForPatientResponse> getListOfDoctors() {

        List<Doctor> doctors = doctorRepository.findAll();

        if (doctors.isEmpty()) {
            throw new EmptyListException("Nie udało się pobrać listy doktorów - lista jest pusta!");
        }

        return doctors.stream().map(doctor -> DoctorDetailsForPatientResponse.builder()
                .doctorID((doctor.getDoctorID()))
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .pwz(doctor.getPwz())
                .specialization(doctor.getSpecialization())
                .dateOfBirth(doctor.getDateBirth())
                .city(doctor.getCity())
                .street(doctor.getStreet())
                .state(doctor.getState())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build()).collect(Collectors.toList());
    }

    public List<DoctorDetailsForPatientResponse> getListOfDoctorsWhoHasSpecificSpecialization(String specialization) {
        List<Doctor> doctorList = doctorRepository.getListOfDoctorsWhoHasSpecificSpecialization(specialization);

        if (doctorList.isEmpty()) {
            throw new EmptyListException("Lista doktorów dla specjalizacji: " + specialization + " jest pusta!");
        }

        return doctorList.stream().map(doctor -> DoctorDetailsForPatientResponse.builder()
                .doctorID((doctor.getDoctorID()))
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .pwz(doctor.getPwz())
                .specialization(doctor.getSpecialization())
                .dateOfBirth(doctor.getDateBirth())
                .city(doctor.getCity())
                .street(doctor.getStreet())
                .state(doctor.getState())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build()).collect(Collectors.toList());
    }

    public List<DoctorDetailsForPatientResponse> getListOfDoctorsWhoWorkInSpecificState(String state) {

        List<Doctor> doctorList = doctorRepository.getListOfDoctorsWhoWorkInSpecificState(state);

        if (doctorList.isEmpty()) {
            throw new EmptyListException("List doktorów dla województwa: " + state + " jest pusta!");
        }

        return doctorList.stream().map(doctor -> DoctorDetailsForPatientResponse.builder()
                .doctorID((doctor.getDoctorID()))
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .pwz(doctor.getPwz())
                .specialization(doctor.getSpecialization())
                .dateOfBirth(doctor.getDateBirth())
                .city(doctor.getCity())
                .street(doctor.getStreet())
                .state(doctor.getState())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build()).collect(Collectors.toList());
    }

    public List<DoctorDetailsForPatientResponse> getListOfDoctorsWithDoubleFilters(String state, String specialization) {

        List<Doctor> doctorList = doctorRepository.getListOfDoctorsWithDoubleFilters(state, specialization);

        if (doctorList.isEmpty()) {
            throw new EmptyListException("List doktorów dla województwa: " + state + " i specjalizacji: "  + specialization + " jest pusta!");
        }

        return doctorList.stream().map(doctor -> DoctorDetailsForPatientResponse.builder()
                .doctorID((doctor.getDoctorID()))
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .pwz(doctor.getPwz())
                .specialization(doctor.getSpecialization())
                .dateOfBirth(doctor.getDateBirth())
                .city(doctor.getCity())
                .street(doctor.getStreet())
                .state(doctor.getState())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build()).collect(Collectors.toList());
    }

    public MessageResponse updatePatientData(Long patientId, UpdatePatientRequest request) {

        if (requestDataValidator.validateUpdateRequests(null, request)) {
            throw new IncompleteRequestException("Żądanie jest niekompletne!");
        }

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Użytkownik o ID: " + patientId + " nie istnieje!");
        }

        Patient patientToUpdate = patient.get();

        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String city = request.getCity();
        String street = request.getStreet();
        String state = request.getState();
        String phone = request.getPhone();
        String email = request.getEmail();

        if (!firstName.equals("") && firstName.length() >= 3) {
            patientToUpdate.setFirstName(firstName);
        }

        if (!lastName.equals("") && lastName.length() >= 2) {
            patientToUpdate.setLastName(request.getLastName());
        }

        if (!city.equals("")) {
            patientToUpdate.setCity(city);
        }

        if (!street.equals("")) {
            patientToUpdate.setStreet(street);
        }

        if (!state.equals("")) {
            patientToUpdate.setState(state);
        }

        if (!phone.equals("") && PhoneNumberValidator.checkIfPhoneNumberIsInvalid(phone)) {
            throw new InvalidFieldException("Podany numer telefonu jest niepoprawny!");
        }

        if (!phone.equals("")) {
            patientToUpdate.setPhone(request.getPhone());
        }

        if (!email.equals("") && EmailValidator.checkIfEmailIsInvalid(email)) {
            throw new InvalidFieldException("Podany email jest niepoprawny!");
        }

        if (!email.equals(patientToUpdate.getEmail())) {
            userExistenceValidator.checkIfUserWithGivenEmailAlreadyExist(email, patientRepository, doctorRepository);
        }

        if (!email.equals("")) {
            patientToUpdate.setEmail(email);
        }

        patientRepository.save(patientToUpdate);

        return new MessageResponse("Pomyślnie zaktualizowano użytkownika!");
    }

    public MessageResponse deletePatientById(Long patientId) {

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Użytkownik o ID = " + patientId + " nie istnieje!");
        }

        patientRepository.delete(patient.get());

        return new MessageResponse("Pomyślnie usunięto użytkownika!");
    }

    public List<VisitDetailsForPatientResponse> getListOfVisits(Long patientId) {

        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            throw new UserNotFoundException("Pacjent o ID = " + patientId + " nie istnieje!");
        }

        List<Visit> visitList = visitRepository.getVisitsByPatientId(patientId);

        return visitList.stream().map(v -> VisitDetailsForPatientResponse.builder()
                .visitId(v.getVisitId())
                .visitDate(v.getVisitDate().toString())
                .visitTime(v.getVisitTime())
                .visitType(v.getVisitType())
                .doctorId(v.getDoctorId())
                .doctorFirstName(v.getDoctorFirstName())
                .doctorLastName(v.getDoctorLastName())
                .symptoms(v.getSymptoms())
                .diagnosis(v.getDiagnosis())
                .prescribedMedications(v.getPrescribedMedications())
                .takingMedications(v.getTakingMedications())
                .recommendations(v.getRecommendations())
                .build()).collect(Collectors.toList());
    }

    public VisitDetailsForPatientResponse getVisitById(Long visitId) {

        Optional<Visit> visitToBeChecked = visitRepository.findById(visitId);

        if (!visitToBeChecked.isPresent()) {
            throw new VisitNotFoundException("Wizyta o ID = " + visitId + " nie istnieje!");
        }

        Visit visit = visitToBeChecked.get();

        return VisitDetailsForPatientResponse.builder()
                .visitId(visitId)
                .visitDate(visit.getVisitDate().toString())
                .visitTime(visit.getVisitTime())
                .visitType(visit.getVisitType())
                .doctorId(visit.getDoctorId())
                .doctorFirstName(visit.getDoctorFirstName())
                .doctorLastName(visit.getDoctorLastName())
                .symptoms(visit.getSymptoms())
                .diagnosis(visit.getDiagnosis())
                .prescribedMedications(visit.getPrescribedMedications())
                .takingMedications(visit.getTakingMedications())
                .recommendations(visit.getRecommendations())
                .build();
    }
}
