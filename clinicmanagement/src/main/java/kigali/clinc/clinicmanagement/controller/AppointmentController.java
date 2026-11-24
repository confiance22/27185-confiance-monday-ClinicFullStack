package kigali.clinc.clinicmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.service.AppointmentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping (value = "/api/appointments")
public class AppointmentController {
    

    @Autowired 
    private AppointmentService appointmentService;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveAppointment(@RequestBody  Appointment appointment){

        String returnedMessage = appointmentService.saveAppointment(appointment);

        return ResponseMapper.created(returnedMessage, "Appointment");
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment){

        String returnedMessage = appointmentService.updateAppointment(id, appointment);

        return ResponseMapper.ok(returnedMessage, "Appointment");
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id){

        String returnedMessage = appointmentService.deleteAppointment(id);

        if(returnedMessage.contains("has prescriptions")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.startsWith("Appointment is deleted")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id){

        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return new ResponseEntity<>("No Appointment found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAllAppointments(){

        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }


    @GetMapping(value = "/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@RequestParam AppointmentStatus status){

        return new ResponseEntity<>(appointmentService.getAppointmentsByStatus(status), HttpStatus.OK);
    }


    @GetMapping(value = "/find/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable UUID patientId){

        return new ResponseEntity<>(appointmentService.getAppointmentsByPatient(patientId), HttpStatus.OK);
    }


    @GetMapping(value = "/find/doctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable UUID doctorId){

        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctor(doctorId), HttpStatus.OK);
    }


    @GetMapping(value = "/query/1/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getPendingAppointmentsByDoctor(
        @PathVariable UUID doctorId,
        @RequestParam AppointmentStatus status
    ){

        return new ResponseEntity<>(appointmentService.getPendingAppointmentsByDoctor(doctorId, status), HttpStatus.OK);
    }


    @GetMapping(value = "/query/2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getPastPendingAppointments(){

        return new ResponseEntity<>(appointmentService.getPastPendingAppointments(), HttpStatus.OK);
    }


    @GetMapping(value = "/query/3", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getAppointmentsCountPerDoctor(){

        return new ResponseEntity<>(appointmentService.getAppointmentsCountPerDoctor(), HttpStatus.OK);
    }


    @GetMapping(value = "/query/4/{specializationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsBySpecialization(
        @PathVariable UUID specializationId
    ){

        return new ResponseEntity<>(appointmentService.getAppointmentsBySpecialization(specializationId), HttpStatus.OK);
    }


    @GetMapping(value = "/query/5/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Appointment>> getAppointmentsByDoctorPage(
        @PathVariable UUID doctorId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "appointmentDate"));

        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctorPage(doctorId, pageable), HttpStatus.OK);
    }


    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(@RequestParam String start, @RequestParam String end){

        List<Appointment> appointments = appointmentService.getAppointmentsBetween(
            LocalDate.parse(start),
            LocalDate.parse(end)
        );

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @GetMapping(value = "/stats/by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getAppointmentsCountByStatus(){

        return new ResponseEntity<>(appointmentService.getAppointmentsCountByStatus(), HttpStatus.OK);
    }


    @PatchMapping(value = "/cancel-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelAppointmentsOnDate(@RequestParam UUID doctorId, @RequestParam String date){

        String returnedMessage = appointmentService.cancelAppointmentsOnDate(doctorId, LocalDate.parse(date));

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }


    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeAppointmentStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status){

        String returnedMessage = appointmentService.changeAppointmentStatus(id, status);

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.startsWith("Cannot change status")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }


    @PatchMapping(value = "/{id}/reschedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rescheduleAppointment(@PathVariable UUID id, @RequestParam String newDate){

        String returnedMessage = appointmentService.rescheduleAppointment(id, LocalDate.parse(newDate));

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.equalsIgnoreCase("Doctor is already booked on that date")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        if(returnedMessage.startsWith("Appointment rescheduled")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Appointment>> getAppointmentsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "appointmentDate,asc") String sort
    ){

        String[] sortParts = sort.split(",");

        Sort.Direction direction = Sort.Direction.ASC;

        if(sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")){
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));

        return new ResponseEntity<>(appointmentService.getAppointmentsPage(pageable), HttpStatus.OK);
    }


    @DeleteMapping(value = "/cancelled-before", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCancelledAppointmentsBefore(@RequestParam String date){

        String returnedMessage = appointmentService.deleteCancelledAppointmentsBefore(LocalDate.parse(date));

        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

}
