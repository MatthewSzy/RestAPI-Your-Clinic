package com.surgery.validator;

import com.surgery.entity.Doctor;
import com.surgery.entity.Patient;
import com.surgery.exception.user.UserAlreadyExistsException;
import com.surgery.repository.DoctorRepository;
import com.surgery.repository.PatientRepository;
import org.springframework.stereotype.Component;

@Component
public class UserExistenceValidator {

    private UserExistenceValidator() {}

    public void checkIfUserWithGivenEmailAlreadyExist(String email,
                                                             PatientRepository patientRepository,
                                                             DoctorRepository doctorRepository) {

        Patient patient = patientRepository.getPatientByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByEmail(email);

        if (patient != null || doctor != null) {
            throw new UserAlreadyExistsException("Użytkownik o podanym emailu już istnieje!");
        }
    }

    public void checkIfUserWithGivenPeselAlreadyExist(String pesel,
                                                             PatientRepository patientRepository,
                                                             DoctorRepository doctorRepository) {

        Patient patient = patientRepository.getPatientByPesel(pesel);
        Doctor doctor = doctorRepository.getDoctorByPesel(pesel);

        if (patient != null || doctor != null) {
            throw new UserAlreadyExistsException("Użytkownik o podanym peselu już istnieje!");
        }
    }
}
