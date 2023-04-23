package com.example.zlagodasb.employee;

import com.example.zlagodasb.check.Check;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Employee {
    public static final String TABLE_NAME = "Employee";

    private String idEmployee;
    private String password;
    private String emplSurname;
    private String emplName;
    private String emplPatronymic;
    private String emplRole;
    private BigDecimal salary;
    private Date dateOfBirth;
    private Date dateOfStart;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;

    private List<Check> checks;
}
