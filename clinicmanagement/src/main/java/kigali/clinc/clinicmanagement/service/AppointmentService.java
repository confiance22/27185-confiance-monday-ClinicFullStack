package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service 
public class AppointmentService {
    

    @Autowired 
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;


    @Transactional
    public String saveAppointment(Appointment appointment){

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        String validationMessage = validateAppointment(appointment);

        if(validationMessage != null){
            return validationMessage;
        }

        if(appointment.getAppointmentDate().isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }

        String referenceMessage = checkGivenReferences(appointment);

        if(referenceMessage != null){
            return referenceMessage;
        }

        boolean doctorIsBooked = appointmentRepo.existsActiveBooking(
            appointment.getDoctor().getId(),
            appointment.getAppointmentDate(),
            AppointmentStatus.CANCELLED
        );

        if(doctorIsBooked){
            return "Doctor is already booked on that date";
        }

        appointment.setId(null);
        appointment.setReason(appointment.getReason().trim());
        appointment.setPatient(resolvePatient(appointment.getPatient()));
        appointment.setDoctor(resolveDoctor(appointment.getDoctor()));

        appointmentRepo.save(appointment);

        return "Appointment is saved Successfully";
    }


    @Transactional
    public String updateAppointment(UUID id, Appointment appointment){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "No Appointment found with id "+id;
        }

        String validationMessage = validateAppointment(appointment);

        if(validationMessage != null){
            return validationMessage;
        }

        String referenceMessage = checkGivenReferences(appointment);

        if(referenceMessage != null){
            return referenceMessage;
        }

        Appointment appointmentToUpdate = existingAppointment.get();

        appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
        appointmentToUpdate.setReason(isBlank(appointment.getReason()) ? null : appointment.getReason().trim());
        appointmentToUpdate.setPatient(resolvePatient(appointment.getPatient()));
        appointmentToUpdate.setDoctor(resolveDoctor(appointment.getDoctor()));

        appointmentRepo.save(appointmentToUpdate);

        return "Appointment is updated Successfully";
    }


    @Transactional
    public String deleteAppointment(UUID id){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "No Appointment found with id "+id;
        }

        if(prescriptionRepo.existsByAppointmentId(id)){
            return "That appointment has prescriptions, delete them first";
        }

        appointmentRepo.delete(existingAppointment.get());

        return "Appointment is deleted Successfully";
    }


    @Transactional
    public String changeAppointmentStatus(UUID id, AppointmentStatus newStatus){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();

        if(!isAllowedTransition(appointment.getStatus(), newStatus)){
            return "Cannot change status from " + appointment.getStatus() + " to " + newStatus;
        }

        appointment.setStatus(newStatus);

        appointmentRepo.save(appointment);

        return "Appointment status updated successfully";
    }


    @Transactional
    public String rescheduleAppointment(UUID id, LocalDate newDate){

        Optional<Appointment> existingAppointment = appointmentRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();

        if(appointment.getStatus() == AppointmentStatus.COMPLETED){
            return "A COMPLETED appointment cannot be rescheduled";
        }

        if(appointment.getStatus() == AppointmentStatus.CANCELLED){
            return "A CANCELLED appointment cannot be rescheduled";
        }

        if(newDate == null || newDate.isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }

        boolean doctorIsBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
            appointment.getDoctor().getId(),
            newDate,
            AppointmentStatus.CANCELLED
        );

        if(doctorIsBooked){
            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepo.save(appointment);

        return "Appointment rescheduled successfully";
    }


    public Appointment getAppointmentById(UUID id){

        return appointmentRepo.findById(id).orElse(null);
    }


    public List<Appointment> getAllAppointments(){

        return appointmentRepo.findAll();
    }


    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status){

        if(status == null){
            return new ArrayList<>();
        }

        return appointmentRepo.findAppointmentsByStatus(status);
    }


    public List<Appointment> getAppointmentsByPatient(UUID patientId){

        if(patientRepo.findById(patientId).isEmpty()){
            return new ArrayList<>();
        }

        return appointmentRepo.findByPatientId(patientId);
    }


    public List<Appointment> getAppointmentsByDoctor(UUID doctorId){

        if(doctorRepo.findById(doctorId).isEmpty()){
            return new ArrayList<>();
        }

        return appointmentRepo.findByDoctorId(doctorId);
    }


    public List<Appointment> getPendingAppointmentsByDoctor(UUID doctorId, AppointmentStatus status){

        if(doctorRepo.findById(doctorId).isEmpty()){
            return new ArrayList<>();
        }

        return appointmentRepo.findByDoctorIdAndStatusOrderByAppointmentDateAsc(doctorId, status);
    }


    public List<Object[]> getPastPendingAppointments(){

        return appointmentRepo.findPastPendingAppointments(AppointmentStatus.PENDING, LocalDate.now());
    }


    public List<Object[]> getAppointmentsCountPerDoctor(){

        return appointmentRepo.countAppointmentsPerDoctor();
    }


    public List<Appointment> getAppointmentsBySpecialization(UUID specializationId){

        return appointmentRepo.findAppointmentsBySpecialization(specializationId);
    }


    public Page<Appointment> getAppointmentsByDoctorPage(UUID doctorId, Pageable pageable){

        return appointmentRepo.findByDoctorIdOrderByAppointmentDateAsc(doctorId, pageable);
    }


    private String validateAppointment(Appointment appointment){

        if(appointment.getAppointmentDate() == null){
            return "Appointment date is required";
        }

        if(appointment.getStatus() == null){
            return "Appointment status is required";
        }

        if(isBlank(appointment.getReason())){
            return "Appointment reason is required";
        }

        if(appointment.getPatient() == null){
            return "Appointment patient is required";
        }

        if(appointment.getDoctor() == null){
            return "Appointment doctor is required";
        }

        return null;
    }


    private String checkGivenReferences(Appointment appointment){

        if(appointment.getPatient().getId() == null){
            return "Patient id is required for an Appointment";
        }

        if(patientRepo.findById(appointment.getPatient().getId()).isEmpty()){
            return "No Patient found with id "+appointment.getPatient().getId();
        }

        if(appointment.getDoctor().getId() == null){
            return "Doctor id is required for an Appointment";
        }

        if(doctorRepo.findById(appointment.getDoctor().getId()).isEmpty()){
            return "No Doctor found with id "+appointment.getDoctor().getId();
        }

        return null;
    }


    private Patient resolvePatient(Patient patient){

        return patientRepo.findById(patient.getId()).orElse(null);
    }


    private Doctor resolveDoctor(Doctor doctor){

        return doctorRepo.findById(doctor.getId()).orElse(null);
    }


    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end){

        return appointmentRepo.findAppointmentsBetween(start, end);
    }


    public List<Object[]> getAppointmentsCountByStatus(){

        return appointmentRepo.countAppointmentsByStatus();
    }


    public Page<Appointment> getAppointmentsPage(Pageable pageable){

        return appointmentRepo.findAll(pageable);
    }


    @Transactional
    public String cancelAppointmentsOnDate(UUID doctorId, LocalDate date){

        int cancelledAppointments = appointmentRepo.cancelAppointmentsOnDate(
            doctorId,
            date,
            AppointmentStatus.CANCELLED,
            AppointmentStatus.COMPLETED
        );

        return cancelledAppointments + " appointments cancelled";
    }


    @Transactional
    public String deleteCancelledAppointmentsBefore(LocalDate date){

        int deletedAppointments = appointmentRepo.deleteCancelledAppointmentsBefore(
            AppointmentStatus.CANCELLED,
            date
        );

        return deletedAppointments + " appointments deleted";
    }


    private boolean isAllowedTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus){

        if(currentStatus == AppointmentStatus.SCHEDULED){
            return newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED;
        }

        if(currentStatus == AppointmentStatus.CONFIRMED){
            return newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED;
        }

        return false;
    }


    private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}