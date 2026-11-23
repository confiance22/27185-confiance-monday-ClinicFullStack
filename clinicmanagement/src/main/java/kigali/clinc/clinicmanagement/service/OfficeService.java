package kigali.clinc.clinicmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;

@Service 
public class OfficeService {
    

    @Autowired 
    private OfficeRepository officeRepo;

    @Autowired
    private DoctorRepository doctorRepo;


    @Transactional
    public String saveOffice(Office office){

        if(isBlank(office.getName())){
            return "Office name is required";
        }

        if(office.getOfficeNumber() <= 0){
            return "Office number must be a positive number";
        }

        if(officeRepo.existsByOfficeNumber(office.getOfficeNumber())){
            return "Office with that number "+office.getOfficeNumber()+ " already exists";
        }

        office.setId(null);
        office.setName(office.getName().trim());

        officeRepo.save(office);

        return "Office is saved Successfully";
    }


    @Transactional
    public String updateOffice(UUID id, Office office){

        Optional<Office> existingOffice = officeRepo.findById(id);

        if(existingOffice.isEmpty()){
            return "No Office found with id "+id;
        }

        if(isBlank(office.getName())){
            return "Office name is required";
        }

        if(office.getOfficeNumber() <= 0){
            return "Office number must be a positive number";
        }

        Optional<Office> officeWithThatNumber = officeRepo.findByOfficeNumber(office.getOfficeNumber());

        if(officeWithThatNumber.isPresent() && !officeWithThatNumber.get().getId().equals(id)){
            return "Office with that number "+office.getOfficeNumber()+ " already exists";
        }

        Office officeToUpdate = existingOffice.get();

        officeToUpdate.setName(office.getName().trim());
        officeToUpdate.setOfficeNumber(office.getOfficeNumber());

        officeRepo.save(officeToUpdate);

        return "Office is updated Successfully";
    }


    @Transactional
    public String deleteOffice(UUID id){

        Optional<Office> existingOffice = officeRepo.findById(id);

        if(existingOffice.isEmpty()){
            return "No Office found with id "+id;
        }

        if(doctorRepo.findByOfficeId(id).isPresent()){
            return "Office is already assigned to a Doctor, remove the Doctor first";
        }

        officeRepo.delete(existingOffice.get());

        return "Office is deleted Successfully";
    }


    public Office getOfficeById(UUID id){

        return officeRepo.findById(id).orElse(null);
    }


    public List<Office> getAllOffices(){

        return officeRepo.findAll();
    }


    public List<Office> getOfficesByName(String name){

        if(isBlank(name)){
            return new ArrayList<>();
        }

        return officeRepo.findByNameContainingIgnoreCase(name.trim());
    }


    public List<Office> getOfficesByDoctor(UUID doctorId){

        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        if(doctor == null || doctor.getOffice() == null){
            return new ArrayList<>();
        }

        List<Office> offices = new ArrayList<>();
        offices.add(doctor.getOffice());
        return offices;
    }


    public List<Object[]> getBusiestOffice(){

        return officeRepo.findBusiestOffice();
    }

    public List<Office> getFreeOffices(){

        return officeRepo.findFreeOffices();
    }


    private boolean isBlank(String value){

        return value == null || value.trim().isEmpty();
    }
}
