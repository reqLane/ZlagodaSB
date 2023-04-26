package com.example.zlagodasb.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@ToString
public class EmployeeInfo {
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
    private String street;
    private String zipCode;
}
