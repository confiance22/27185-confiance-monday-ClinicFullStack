package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.service.AppointmentService;
import kigali.clinc.clinicmanagement.service.PrescriptionService;



@RestController 
@RequestMapping (value = "/api/prescriptions")
public class PrescriptionController {
    

    @Autowired 
    private PrescriptionService prescriptionService;

    @Autowired
    private AppointmentService appointmentService;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  savePrescription(@RequestParam UUID appointmentId, @RequestBody  Prescription prescription){

        String returnedMessage = prescriptionService.savePrescription(appointmentId, prescription);

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.startsWith("Prescription saved")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/appointment/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  getPrescriptionsByAppointment(@PathVariable UUID appointmentId){

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            return new ResponseEntity<>("The appointment with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(prescriptionService.getPrescriptionsByAppointment(appointmentId), HttpStatus.OK);
    }


    @GetMapping(value = "/patient/{ssNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String ssNumber){

        return new ResponseEntity<>(prescriptionService.getPrescriptionsByPatient(ssNumber), HttpStatus.OK);
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updatePrescription(@PathVariable UUID id, @RequestBody  Prescription prescription){

        String returnedMessage = prescriptionService.updatePrescription(id, prescription);

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.startsWith("Prescription updated")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePrescription(@PathVariable UUID id){

        String returnedMessage = prescriptionService.deletePrescription(id);

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.startsWith("Prescription deleted")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/stats/top-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getTopMedicines(){

        return new ResponseEntity<>(prescriptionService.getTopMedicines(), HttpStatus.OK);
    }

}