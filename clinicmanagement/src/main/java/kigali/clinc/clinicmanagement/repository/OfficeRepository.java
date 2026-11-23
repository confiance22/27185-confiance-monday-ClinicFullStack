package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Office;



@Repository 
public interface OfficeRepository extends  JpaRepository<Office,UUID >{

    Optional<Office> findByOfficeNumber(int officeNumber);

    Optional<Office> findByNameAndOfficeNumber(String name, int officeNumber);

    List<Office> findByNameContainingIgnoreCase(String name);

    Boolean existsByOfficeNumber(int officeNumber);

    @Query("SELECT o FROM Office o WHERE NOT EXISTS "
        + "(SELECT d FROM Doctor d WHERE d.office = o) "
        + "ORDER BY o.officeNumber ASC")
    List<Office> findFreeOffices();

    @Query("SELECT o.name, o.officeNumber, COUNT(a.id) FROM Appointment a "
        + "JOIN a.doctor d JOIN d.office o "
        + "GROUP BY o.id, o.name, o.officeNumber "
        + "ORDER BY COUNT(a.id) DESC")
    List<Object[]> findBusiestOffice();
}