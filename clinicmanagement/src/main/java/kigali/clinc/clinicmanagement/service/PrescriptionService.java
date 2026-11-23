package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service 
public class PrescriptionService {
    
    @Autowired 
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;


    @Transactional
    public String savePrescription(UUID appointmentId, Prescription prescription){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(appointmentId);

        if(existingAppointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();

        if(appointment.getStatus() != AppointmentStatus.COMPLETED){
            return "Prescriptions are only for COMPLETED appointments";
        }

        if(isBlank(prescription.getMedicineName())){
            return "Prescription medicine name is required";
        }

        if(isBlank(prescription.getDosage())){
            return "Prescription dosage is required";
        }

        if(prescription.getDurationDays() <= 0){
            return "Prescription duration days must be greater than 0";
        }

        Prescription prescriptionToSave = new Prescription();

        prescriptionToSave.setMedicineName(prescription.getMedicineName().trim());
        prescriptionToSave.setDosage(prescription.getDosage().trim());
        prescriptionToSave.setDurationDays(prescription.getDurationDays());
        prescriptionToSave.setIssuedDate(LocalDate.now());
        prescriptionToSave.setAppointment(appointment);

        prescriptionRepo.save(prescriptionToSave);

        return "Prescription saved successfully";
    }


    @Transactional
    public String updatePrescription(UUID id, Prescription prescription){

        Optional<Prescription> existingPrescription = prescriptionRepo.findById(id);

        if(existingPrescription.isEmpty()){
            return "The prescription with that id does not exist";
        }

        if(isBlank(prescription.getMedicineName()) || isBlank(prescription.getDosage()) || prescription.getDurationDays() <= 0){
            return "All prescription fields are required";
        }

        Prescription prescriptionToUpdate = existingPrescription.get();

        prescriptionToUpdate.setMedicineName(prescription.getMedicineName().trim());
        prescriptionToUpdate.setDosage(prescription.getDosage().trim());
        prescriptionToUpdate.setDurationDays(prescription.getDurationDays());

        prescriptionRepo.save(prescriptionToUpdate);

        return "Prescription updated successfully";
    }


    @Transactional
    public String deletePrescription(UUID id){

        Optional<Prescription> existingPrescription = prescriptionRepo.findById(id);

        if(existingPrescription.isEmpty()){
            return "The prescription with that id does not exist";
        }

        prescriptionRepo.delete(existingPrescription.get());

        return "Prescription deleted successfully";
    }


    public List<Prescription> getPrescriptionsByAppointment(UUID appointmentId){

        return prescriptionRepo.findByAppointmentId(appointmentId);
    }


    public List<Prescription> getPrescriptionsByPatient(String ssNumber){

        return prescriptionRepo.findPrescriptionsByPatientSsNumber(ssNumber);
    }


    public List<Object[]> getTopMedicines(){

        return prescriptionRepo.countPrescriptionsPerMedicine();
    }


    private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}