package com.example.zlagodasb.customer_card;

import com.example.zlagodasb.check.Check;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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

    private List<Check> checks;
}
