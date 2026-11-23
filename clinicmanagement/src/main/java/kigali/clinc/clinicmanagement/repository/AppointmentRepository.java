package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Patient;



@Repository
public interface AppointmentRepository extends  JpaRepository<Appointment,UUID >{

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByDoctorId(UUID doctorId);

    Boolean existsByPatientId(UUID patientId);

    Boolean existsByDoctorId(UUID doctorId);

    Boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);

    List<Appointment> findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
        String ssNumber, LocalDate appointmentDate, List<AppointmentStatus> statuses);

    Long countByAppointmentDate(LocalDate appointmentDate);

    Long countByStatus(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsByStatus(@Param("status") AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :start AND :end ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END "
        + "FROM Appointment a "
        + "WHERE a.doctor.id = :doctorId "
        + "AND a.appointmentDate = :appointmentDate "
        + "AND a.status <> :status")
    Boolean existsActiveBooking(
        @Param("doctorId") UUID doctorId,
        @Param("appointmentDate") LocalDate appointmentDate,
        @Param("status") AppointmentStatus status
    );

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findDistinctPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status ORDER BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Query("SELECT a.patient FROM Appointment a GROUP BY a.patient "
        + "HAVING COUNT(a.id) >= :min ORDER BY COUNT(a.id) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :newStatus "
        + "WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date "
        + "AND a.status <> :skipStatus")
    int cancelAppointmentsOnDate(
        @Param("doctorId") UUID doctorId,
        @Param("date") LocalDate date,
        @Param("newStatus") AppointmentStatus newStatus,
        @Param("skipStatus") AppointmentStatus skipStatus
    );

    @Modifying
    @Query("DELETE FROM Appointment a "
        + "WHERE a.status = :status "
        + "AND a.appointmentDate < :date")
    int deleteCancelledAppointmentsBefore(
        @Param("status") AppointmentStatus status,
        @Param("date") LocalDate date
    );

    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(UUID doctorId, AppointmentStatus status);

    @Query("SELECT CONCAT(p.firstName, ' ', p.lastName), "
        + "CONCAT(d.firstName, ' ', d.lastName), "
        + "a.appointmentDate "
        + "FROM Appointment a "
        + "JOIN a.patient p "
        + "JOIN a.doctor d "
        + "WHERE a.status = :status AND a.appointmentDate < :today "
        + "ORDER BY a.appointmentDate ASC")
    List<Object[]> findPastPendingAppointments(
        @Param("status") AppointmentStatus status,
        @Param("today") LocalDate today
    );

    @Query("SELECT d, COUNT(a) FROM Doctor d JOIN d.appointments a "
        + "GROUP BY d "
        + "ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT DISTINCT a FROM Appointment a "
        + "JOIN a.doctor d "
        + "JOIN d.specializations s "
        + "WHERE s.id = :specializationId "
        + "ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsBySpecialization(@Param("specializationId") UUID specializationId);

    Page<Appointment> findByDoctorIdOrderByAppointmentDateAsc(UUID doctorId, Pageable pageable);
}
