package kigali.clinc.clinicmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.service.DoctorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping (value = "/api/doctors")
public class DoctorController {
    

    @Autowired 
    private DoctorService doctorService;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveDoctor(@RequestBody  Doctor doctor){

        String returnedMessage = doctorService.saveDoctor(doctor);

        return ResponseMapper.created(returnedMessage, "Doctor");
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor){

        String returnedMessage = doctorService.updateDoctor(id, doctor);

        return ResponseMapper.ok(returnedMessage, "Doctor");
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id){

        String returnedMessage = doctorService.deleteDoctor(id);

        return ResponseMapper.ok(returnedMessage, "Doctor");
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){

        Doctor doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return new ResponseEntity<>("No Doctor found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getAllDoctors(){

        return new ResponseEntity<>(doctorService.getAllDoctors(), HttpStatus.OK);
    }


    @GetMapping(value = "/find/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByFirstName(@RequestParam String firstName){

        return new ResponseEntity<>(doctorService.getDoctorsByFirstName(firstName), HttpStatus.OK);
    }


    @GetMapping(value = "/find/lastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByLastName(@RequestParam String lastName){

        return new ResponseEntity<>(doctorService.getDoctorsByLastName(lastName), HttpStatus.OK);
    }


    @GetMapping(value = "/find/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsByFullName(@RequestParam String firstName, @RequestParam String lastName){

        return new ResponseEntity<>(doctorService.getDoctorsByFullName(firstName, lastName), HttpStatus.OK);
    }


    @GetMapping(value = "/find/specialization/{specializationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable UUID specializationId){

        return new ResponseEntity<>(doctorService.getDoctorsBySpecialization(specializationId), HttpStatus.OK);
    }


    @GetMapping(value = "/by-specialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsBySpecializationName(@RequestParam String name){

        return new ResponseEntity<>(doctorService.getDoctorsBySpecializationName(name), HttpStatus.OK);
    }


    @GetMapping(value = "/without-office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> getDoctorsWithoutOffice(){

        return new ResponseEntity<>(doctorService.getDoctorsWithoutOffice(), HttpStatus.OK);
    }


    @PutMapping(value = "/{doctorId}/specializations/{specializationId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addSpecializationToDoctor(@PathVariable UUID doctorId, @PathVariable UUID specializationId){

        String returnedMessage = doctorService.addSpecializationToDoctor(doctorId, specializationId);

        if(returnedMessage.equalsIgnoreCase("Specialization added to the doctor")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.equalsIgnoreCase("The doctor already has that specialization")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(value = "/{doctorId}/specializations/{specializationId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> removeSpecializationFromDoctor(@PathVariable UUID doctorId, @PathVariable UUID specializationId){

        String returnedMessage = doctorService.removeSpecializationFromDoctor(doctorId, specializationId);

        if(returnedMessage.equalsIgnoreCase("Specialization removed from the doctor")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.equalsIgnoreCase("The doctor does not have that specialization")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @PatchMapping(value = "/{doctorId}/office", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> moveDoctorToOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber){

        String returnedMessage = doctorService.moveDoctorToOffice(doctorId, officeNumber);

        if(returnedMessage.equalsIgnoreCase("Office changed successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        if(returnedMessage.contains("does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.equalsIgnoreCase("That office is taken by other doctor")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/{doctorId}/agenda", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoctorAgenda(@PathVariable UUID doctorId, @RequestParam String date){

        Doctor doctor = doctorService.getDoctorById(doctorId);

        if (doctor == null) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctorService.getDoctorAgenda(doctorId, LocalDate.parse(date)), HttpStatus.OK);
    }


    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Doctor>> searchDoctorsByName(@RequestParam String name){

        return new ResponseEntity<>(doctorService.searchDoctorsByName(name), HttpStatus.OK);
    }


    @GetMapping(value = "/workload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> getDoctorWorkload(@RequestParam String start, @RequestParam String end){

        return new ResponseEntity<>(doctorService.getDoctorWorkload(LocalDate.parse(start), LocalDate.parse(end)), HttpStatus.OK);
    }

}
