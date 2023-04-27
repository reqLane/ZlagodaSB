package com.example.zlagodasb.customer_card;

import com.example.zlagodasb.customer_card.model.CustomerCardInfo;
import com.example.zlagodasb.customer_card.model.CustomerCardModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerCardService {
    private final CustomerCardRepo customerCardRepo;

    @Autowired
    public CustomerCardService(CustomerCardRepo customerCardRepo) {
        this.customerCardRepo = customerCardRepo;
    }

    //OPERATIONS

    public List<CustomerCard> findAllCardsSortedBySurname() {
        return customerCardRepo.findAllSortedByCustSurname();
    }

    public List<CustomerCard> findAllWithPercentSortedBySurname(Integer percent) {
        return customerCardRepo.findAllWithPercentSortedBySurname(percent);
    }

    public List<CustomerCard> findAllBySurname(String custSurname) {
        return customerCardRepo.findAllByCustSurname(custSurname);
    }

    public List<CustomerCardInfo> getClientsWhoBoughtMoreThanAverage() {
        return customerCardRepo.getClientsWhoBoughtMoreThanAverage();
    }

    public List<CustomerCard> getClientsWhoBoughtAllProducts() {
        return customerCardRepo.getClientsWhoBoughtAllProducts();
    }

    //DEFAULT OPERATIONS

    public List<CustomerCard> findAll() {
        return customerCardRepo.findAll();
    }

    public CustomerCard findById(String cardNumber) {
        return customerCardRepo.findById(cardNumber);
    }

    public CustomerCard create(CustomerCardModel customerCardModel) {
        CustomerCard entity = customerCardModel.toEntity();
        String uuid = UUID.randomUUID().toString();
        String cardNumber = uuid.substring(0, 4) + uuid.substring(9, 11) + uuid.substring(14, 16) + uuid.substring(19, 21) + uuid.substring(24, 27);
        entity.setCardNumber(cardNumber);
        return customerCardRepo.create(entity);
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
