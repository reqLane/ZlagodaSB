package com.example.zlagodasb.check;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CheckService {
    private CheckRepo checkRepo;

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<Check> findAll() {
        return checkRepo.findAll();
    }

    public Check findById(String checkNumber) {
        return checkRepo.findById(checkNumber);
    }

    public Check create(Check check) {
        return checkRepo.create(check);
    }

    public void update(Check check) {
        checkRepo.update(check);
    }

    public void deleteById(String checkNumber){
        checkRepo.deleteById(checkNumber);
    }

    public void delete(@NonNull Collection<Check> list){
        checkRepo.delete(list);
    }

    public void deleteAll(){
        checkRepo.deleteAll();
    }
}
