package com.surgery.repository;

import com.surgery.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE d.email LIKE ?1")
    Doctor getDoctorByEmail(String email);

    @Query("SELECT d FROM Doctor d where d.pesel LIKE ?1")
    Doctor getDoctorByPesel(String pesel);

    @Query("SELECT d.password FROM Doctor d WHERE d.email LIKE ?1")
    String getPasswordByEmail(String email);

    @Query("SELECT d FROM Doctor d where d.specialization LIKE ?1")
    List<Doctor> getListOfDoctorsWhoHasSpecificSpecialization(String specialization);

    @Query("SELECT d FROM Doctor d where d.state LIKE ?1")
    List<Doctor> getListOfDoctorsWhoWorkInSpecificState(String state);

    @Query("SELECT d FROM Doctor d where d.state LIKE ?1 AND d.specialization LIKE ?2")
    List<Doctor> getListOfDoctorsWithDoubleFilters(String state, String specialization);

}

