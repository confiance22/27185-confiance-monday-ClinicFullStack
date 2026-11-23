package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.service.OfficeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping (value = "/api/offices")
public class OfficeController {
    

    @Autowired 
    private OfficeService officeServes;


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?>  saveOffice(@RequestBody  Office office){

        String returnedMessage = officeServes.saveOffice(office);

        return ResponseMapper.created(returnedMessage, "Office");
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office){

        String returnedMessage = officeServes.updateOffice(id, office);

        return ResponseMapper.ok(returnedMessage, "Office");
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id){

        String returnedMessage = officeServes.deleteOffice(id);

        return ResponseMapper.ok(returnedMessage, "Office");
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id){

        Office office = officeServes.getOfficeById(id);

        if (office == null) {
            return new ResponseEntity<>("No Office found with id "+id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(office, HttpStatus.OK);
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Office>> getAllOffices(){

        return new ResponseEntity<>(officeServes.getAllOffices(), HttpStatus.OK);
    }


    @GetMapping(value = "/find/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Office>> getOfficesByName(@RequestParam String name){

        return new ResponseEntity<>(officeServes.getOfficesByName(name), HttpStatus.OK);
    }


    @GetMapping(value = "/find/doctor/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Office>> getOfficesByDoctor(@PathVariable UUID doctorId){

        return new ResponseEntity<>(officeServes.getOfficesByDoctor(doctorId), HttpStatus.OK);
    }


    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Office>> getFreeOffices(){

        return new ResponseEntity<>(officeServes.getFreeOffices(), HttpStatus.OK);
    }


    @GetMapping(value = "/busiest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBusiestOffice(){

        List<Object[]> busiestOffice = officeServes.getBusiestOffice();

        if(busiestOffice.isEmpty()){
            return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
        }

        return new ResponseEntity<>(busiestOffice.get(0), HttpStatus.OK);
    }

}
