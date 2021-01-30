package com.surgery.repository;

import com.surgery.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v FROM Visit v WHERE v.doctorId = ?1")
    List<Visit> getVisitsByDoctorId(Long doctorId);

    @Query("SELECT v FROM Visit v WHERE v.patientId = ?1")
    List<Visit> getVisitsByPatientId(Long patientId);
}
