package com.example.zlagodasb.employee;

import com.example.zlagodasb.check.Check;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Employee")
public class Employee {
    @Id
    private String idEmployee;

    private String emplSurname;
    private String emplName;
    private String emplPatronymic;
    private String emplRole;
    private BigDecimal salary;
    private Date dateOfBirth;
    private Date dateOfStart;
    private String phoneNumber;
    private String city;
    private String zipCode;

    @OneToMany(mappedBy = "employee")
    private List<Check> checks;
}
