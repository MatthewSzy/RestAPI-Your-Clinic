package com.surgery.repository;

import com.surgery.dto.patient.response.PatientDetailsForDoctorResponse;
import com.surgery.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.email LIKE ?1")
    Patient getPatientByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.pesel LIKE ?1")
    Patient getPatientByPesel(String pesel);

    @Query("SELECT p.password FROM Patient p WHERE p.email LIKE ?1")
    String getPasswordByEmail(String email);

}
