package com.example.zlagodasb.check;

import com.example.zlagodasb.CustomerCard.CustomerCard;
import com.example.zlagodasb.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name="Check")
public class Check {
    @Id
    private String checkNumber;

    //
    //

    private LocalDateTime printDate;

    private BigDecimal sumTotal;
    private BigDecimal vat;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerCard card;

}
