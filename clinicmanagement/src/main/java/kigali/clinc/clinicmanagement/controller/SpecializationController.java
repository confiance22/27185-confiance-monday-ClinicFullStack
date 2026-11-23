package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.service.SpecializationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping (value = "/api/specializations")
public class SpecializationController {
    

    @Autowired 
    private SpecializationService specializationService;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveSpecialization(@RequestBody  Specialization specialization){

        String returnedMessage = specializationService.saveSpecialization(specialization);

        return ResponseMapper.created(returnedMessage, "Specialization");
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateSpecialization(@PathVariable UUID id, @RequestBody Specialization specialization){

        String returnedMessage = specializationService.updateSpecialization(id, specialization);

        return ResponseMapper.ok(returnedMessage, "Specialization");
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSpecialization(@PathVariable UUID id){

        String returnedMessage = specializationService.deleteSpecialization(id);

        return ResponseMapper.ok(returnedMessage, "Specialization");
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSpecializationById(@PathVariable UUID id){

        Specialization specialization = specializationService.getSpecializationById(id);

        if (specialization == null) {
            return new ResponseEntity<>("No Specialization found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(specialization, HttpStatus.OK);
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specialization>> getAllSpecializations(){

        return new ResponseEntity<>(specializationService.getAllSpecializations(), HttpStatus.OK);
    }


    @GetMapping(value = "/find/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specialization>> getSpecializationsByName(@RequestParam String name){

        return new ResponseEntity<>(specializationService.getSpecializationsByName(name), HttpStatus.OK);
    }


    @GetMapping(value = "/unused", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specialization>> getUnusedSpecializations(){

        return new ResponseEntity<>(specializationService.getUnusedSpecializations(), HttpStatus.OK);
    }

}
