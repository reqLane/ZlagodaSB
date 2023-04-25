package com.example.zlagodasb.customer_card.model;

import com.example.zlagodasb.customer_card.CustomerCard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerCardModel {
    private String custSurname;
    private String custName;
    private String custPatronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
    private Integer percent;

    public CustomerCard toEntity() {
        CustomerCard entity = new CustomerCard();
        entity.setCustSurname(custSurname);
        entity.setCustName(custName);
        entity.setCustPatronymic(custPatronymic);
        entity.setPhoneNumber(phoneNumber);
        entity.setCity(city);
        entity.setStreet(street);
        entity.setZipCode(zipCode);
        entity.setPercent(percent);
        return entity;
    }
}
