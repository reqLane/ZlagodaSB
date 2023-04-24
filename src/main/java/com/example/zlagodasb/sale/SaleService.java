package com.example.zlagodasb.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepo saleRepo;

    @Autowired
    public SaleService(SaleRepo saleRepo) {
        this.saleRepo = saleRepo;
    }

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<Sale> findAll() {
        return saleRepo.findAll();
    }

    public Sale findById(String UPC, String checkNumber) {
        return saleRepo.findById(UPC, checkNumber);
    }

    public Sale create(Sale sale) {
        return saleRepo.create(sale);
    }

    public void update(Sale sale) {
        saleRepo.update(sale);
    }

    public void deleteById(String UPC, String checkNumber){
        saleRepo.deleteById(UPC, checkNumber);
    }

    public void delete(@NonNull Collection<Sale> list){
        saleRepo.delete(list);
    }

    public void deleteAll(){
        saleRepo.deleteAll();
    }
}
