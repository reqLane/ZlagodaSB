package com.example.zlagodasb.employee;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@ToString
public class EmployeeModel {
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

    public Employee toEntity() {
        Employee entity = new Employee();
        entity.setIdEmployee(idEmployee);
        entity.setPassword(password);
        entity.setEmplSurname(emplSurname);
        entity.setEmplName(emplName);
        entity.setEmplPatronymic(emplPatronymic);
        entity.setEmplRole(emplRole);
        entity.setSalary(salary);
        entity.setDateOfBirth(dateOfBirth);
        entity.setDateOfStart(dateOfStart);
        entity.setPhoneNumber(phoneNumber);
        entity.setCity(city);
        entity.setStreet(street);
        entity.setZipCode(zipCode);
        return entity;
    }
}
