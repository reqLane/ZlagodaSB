package com.example.zlagodasb.customer_card;

import com.example.zlagodasb.check.Check;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Customer_Card")
public class CustomerCard {
    @Id
    private String cardNumber;

    private String custSurname;
    private String custName;
    private String custPatronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
    private Integer percent;

    @OneToMany(mappedBy = "card")
    private List<Check> checks;
}
