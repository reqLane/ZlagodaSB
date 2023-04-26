package com.example.zlagodasb.customer_card;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class CustomerCard {
    public static final String TABLE_NAME = "Customer_Card";

    private String cardNumber;
    private String custSurname;
    private String custName;
    private String custPatronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
    private Integer percent;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Card ID", cardNumber);
        map.put("Name", custSurname + " " + custName + (custPatronymic != null ? (" " + custPatronymic) : ""));
        map.put("Phone", phoneNumber);
        map.put("City", city);
        map.put("Street", street);
        map.put("Zip code", zipCode);
        map.put("Discount", percent);
        return map;
    }
}
