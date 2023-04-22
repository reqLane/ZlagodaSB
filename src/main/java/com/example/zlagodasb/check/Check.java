package com.example.zlagodasb.check;

import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.sale.Sale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name="Checks")
public class Check {
    @Id
    private String checkNumber;

    private Date printDate;

    private BigDecimal sumTotal;
    private BigDecimal vat;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerCard card;

    @OneToMany(mappedBy = "PPK.check")
    private List<Sale> sales = new ArrayList<>();
}
