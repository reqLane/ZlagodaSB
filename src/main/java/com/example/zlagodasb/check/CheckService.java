package com.example.zlagodasb.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class CheckService {
    private final CheckRepo checkRepo;

    @Autowired
    public CheckService(CheckRepo checkRepo) {
        this.checkRepo = checkRepo;
    }

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<Check> findAll() {
        return checkRepo.findAll();
    }

    public Check findById(String checkNumber) {
        return checkRepo.findById(checkNumber);
    }

    public Check create(CheckModel checkModel) {
        Check entity = checkModel.toEntity();
        String uuid = UUID.randomUUID().toString();
        String checkNumber = uuid.substring(0, 4) + uuid.substring(9, 11) + uuid.substring(14, 16) + uuid.substring(19, 21) + uuid.substring(24, 27);
        entity.setCheckNumber(checkNumber);
        return checkRepo.create(entity);
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
