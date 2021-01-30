package com.surgery.validator;

import com.surgery.dto.doctor.request.CreateDoctorRequest;
import com.surgery.dto.doctor.request.UpdateDoctorRequest;
import com.surgery.dto.patient.request.CreatePatientRequest;
import com.surgery.dto.patient.request.UpdatePatientRequest;
import com.surgery.exception.empty.EmptyFieldException;
import com.surgery.exception.invalid.InvalidFieldException;
import com.surgery.exception.invalid.InvalidFieldLengthException;
import com.surgery.validator.fields.*;
import org.springframework.stereotype.Component;

@Component
public class RequestDataValidator {

    private RequestDataValidator() {}

    public void validatePatientRegisterData(CreatePatientRequest request) {

        if (request.getFirstName() == null ||
            request.getLastName() == null ||
            request.getCity() == null ||
            request.getStreet() == null ||
            request.getState() == null ||
            request.getPesel() == null ||
            request.getPhone() == null ||
            request.getEmail() == null ||
            request.getPassword() == null) {

            throw new EmptyFieldException("Jedno z pól jest puste! Proszę o ponowne wprowadzenie danych!");
        }

        validateDataFromRequest(request.getFirstName(), request.getLastName(), request.getPesel(),
                request.getPhone(), request.getEmail(), request.getPassword(), null);
    }

    public void validateDoctorRegisterData(CreateDoctorRequest request) {

        if (request.getFirstName() == null ||
            request.getLastName() == null ||
            request.getPwz() == null ||
            request.getSpecialization() == null ||
            request.getCity() == null ||
            request.getStreet() == null ||
            request.getState() == null ||
            request.getPesel() == null ||
            request.getPhone() == null ||
            request.getEmail() == null ||
            request.getPassword() == null) {

            throw new EmptyFieldException("Jedno z pól jest puste! Proszę o ponowne wprowadzenie danych!");
        }

        validateDataFromRequest(request.getFirstName(), request.getLastName(), request.getPesel(),
                request.getPhone(), request.getEmail(), request.getPassword(), request.getPwz());
    }

    private void validateDataFromRequest(String firstName, String lastName, String pesel,
                                         String phone, String email, String password, String pwz) {

        if (firstName.length() < 3) {
            throw new InvalidFieldLengthException("Podane imię jest za krótkie!");
        }

        if (lastName.length() < 2) {
            throw new InvalidFieldLengthException("Podane nazwisko jest za krótkie!");
        }

        if (pwz != null && PwzValidator.checkIfPwzIsInvalid(pwz)) {
            throw new InvalidFieldException("Podany numer PWZ jest niepoprawny!");
        }

        if (PeselValidator.checkIfPeselNumberIsInvalid(pesel) ||
                DateOfBirthValidator.checkIfPeselHasIncorrectDate(pesel)) {

            throw new InvalidFieldException("Podany pesel jest niepoprawny!");
        }

        if (PhoneNumberValidator.checkIfPhoneNumberIsInvalid(phone)) {
            throw new InvalidFieldException("Podany numer telefonu jest niepoprawny!");
        }

        if (EmailValidator.checkIfEmailIsInvalid(email)) {
            throw new InvalidFieldException("Podany e-mail jest niepoprawny!");
        }

        if (PasswordValidator.checkIfPasswordIsInvalid(password)) {
            String exceptionMessage = "Podane hasło nie spełnia warunków - hasło musi posiadać co najmniej: " +
                    "1 dużą literę, 1 małą literę, 1 cyfrę oraz 1 znak specjalny";
            throw new InvalidFieldException(exceptionMessage);
        }
    }

    public boolean validateUpdateRequests(UpdateDoctorRequest doctorRequest,
                                                 UpdatePatientRequest patientRequest) {

        if (doctorRequest != null && patientRequest == null) {
            return validateRequestFields(doctorRequest.getFirstName(),
                                        doctorRequest.getLastName(),
                                        doctorRequest.getCity(),
                                        doctorRequest.getStreet(),
                                        doctorRequest.getState(),
                                        doctorRequest.getPhone(),
                                        doctorRequest.getEmail());
        }
        else if (doctorRequest == null && patientRequest != null) {
            return validateRequestFields(patientRequest.getFirstName(),
                                        patientRequest.getLastName(),
                                        patientRequest.getCity(),
                                        patientRequest.getStreet(),
                                        patientRequest.getState(),
                                        patientRequest.getPhone(),
                                        patientRequest.getEmail());
        }
        else {
            return false;
        }
    }

    private boolean validateRequestFields(String firstName, String lastName, String city,
                                                 String street, String state, String phone, String email) {
        if (firstName == null ||
            lastName == null ||
            city == null ||
            street == null ||
            state == null ||
            phone == null ||
            email == null) {

            return false;
        }

        return firstName.equals("") &&
                lastName.equals("") &&
                city.equals("") &&
                street.equals("") &&
                state.equals("") &&
                phone.equals("") &&
                email.equals("");
    }
}
