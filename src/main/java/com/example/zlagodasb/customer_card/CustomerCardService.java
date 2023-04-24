package com.example.zlagodasb.customer_card;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomerCardService {
    private CustomerCardRepo customerCardRepo;

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<CustomerCard> findAll() {
        return customerCardRepo.findAll();
    }

    public CustomerCard findById(String cardNumber) {
        return customerCardRepo.findById(cardNumber);
    }

    public CustomerCard create(CustomerCard customerCard) {
        return customerCardRepo.create(customerCard);
    }

    public void update(CustomerCard customerCard) {
        customerCardRepo.update(customerCard);
    }

    public void deleteById(String cardNumber){
        customerCardRepo.deleteById(cardNumber);
    }

    public void delete(@NonNull Collection<CustomerCard> list){
        customerCardRepo.delete(list);
    }

    public void deleteAll(){
        customerCardRepo.deleteAll();
    }
}
