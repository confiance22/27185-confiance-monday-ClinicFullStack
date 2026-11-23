package kigali.clinc.clinicmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMapper {

    public static ResponseEntity<?> created(String returnedMessage, String entityName){

        if(returnedMessage.equalsIgnoreCase(entityName + " is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }

        return error(returnedMessage);
    }

    public static ResponseEntity<?> ok(String returnedMessage, String entityName){

        if(returnedMessage.equalsIgnoreCase(entityName + " is updated Successfully")
            || returnedMessage.equalsIgnoreCase(entityName + " is deleted Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }

        return error(returnedMessage);
    }

    private static ResponseEntity<?> error(String returnedMessage){

        if(returnedMessage.startsWith("No ")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if(returnedMessage.contains("already")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
    }
}