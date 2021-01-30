package com.surgery.repository;

import com.surgery.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.doctorId = ?1")
    List<Reservation> getReservationsByDoctorId(Long doctorId);

    @Query("SELECT r FROM Reservation r WHERE r.patientId = ?1")
    List<Reservation> getReservationsByPatientId(Long patientId);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = ?1")
    List<Reservation> getReservationsByDate(Date date);

    @Query("SELECT r FROM Reservation r WHERE r.doctorId = ?1 AND r.reservationDate = ?2")
    List<Reservation> getDoctorReservationsByDate(Long doctorId, Date date);
}
