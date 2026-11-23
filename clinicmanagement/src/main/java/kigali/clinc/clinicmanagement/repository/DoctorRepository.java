package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Doctor;



@Repository 
public interface DoctorRepository extends  JpaRepository<Doctor,UUID >{

    List<Doctor> findByFirstName(String firstName);

    List<Doctor> findByLastName(String lastName);

    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);

    List<Doctor> findBySpecializationsId(UUID specializationId);

    Optional<Doctor> findByOfficeId(UUID officeId);

    Optional<Doctor> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, Date dateOfBirth);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.specializations s WHERE UPPER(s.name) = UPPER(:name)")
    List<Doctor> findDoctorsBySpecializationName(@Param("name") String name);

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL")
    List<Doctor> findDoctorsWithoutOffice();

    @Query("SELECT d FROM Doctor d "
        + "WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) "
        + "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) "
        + "ORDER BY d.lastName ASC")
    List<Doctor> searchDoctorsByName(@Param("name") String name);

    @Query("SELECT d.firstName, d.lastName, COUNT(a) FROM Doctor d "
        + "JOIN d.appointments a "
        + "WHERE a.appointmentDate BETWEEN :start AND :end "
        + "AND a.status = :status "
        + "GROUP BY d.id, d.firstName, d.lastName "
        + "ORDER BY COUNT(a) DESC")
    List<Object[]> findDoctorWorkload(
        @Param("start") LocalDate start,
        @Param("end") LocalDate end,
        @Param("status") AppointmentStatus status
    );
}