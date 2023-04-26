package com.example.zlagodasb.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Employee ID", idEmployee);
        map.put("Name", emplSurname + " " + emplName + " " + emplPatronymic);
        map.put("Role", emplRole);
        map.put("Salary", salary);
        map.put("Date of birth", dateOfBirth);
        map.put("Date of start", dateOfStart);
        map.put("Phone", phoneNumber);
        map.put("City", city);
        map.put("Street", street);
        map.put("Zip code", zipCode);
        return map;
    }
}
