package com.example.zlagodasb.employee;

import com.example.zlagodasb.employee.model.EmployeeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

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

    public EmployeeInfo toInfo() {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setIdEmployee(idEmployee);
        employeeInfo.setEmplFullName(emplSurname + " " + emplName + (emplPatronymic != null ? (" " + emplPatronymic) : ""));
        employeeInfo.setEmplRole(emplRole);
        employeeInfo.setSalary(salary);
        employeeInfo.setDateOfBirth(dateOfBirth);
        employeeInfo.setDateOfStart(dateOfStart);
        employeeInfo.setPhoneNumber(phoneNumber);
        employeeInfo.setCity(city);
        employeeInfo.setStreet(street);
        employeeInfo.setZipCode(zipCode);
        employeeInfo.setSoldTotal(null);
        return employeeInfo;
    }
}
