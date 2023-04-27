package com.example.zlagodasb.employee;

import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.employee.model.EmployeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepo = employeeRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //AUTHENTICATION

    public Map<String, String> authenticate(String idEmployee, String password) throws Exception {
        Employee entity = employeeRepo.findById(idEmployee);
        if(entity == null) throw new Exception("Employee with idEmployee not found");

        String expectedIdEmployee = entity.getIdEmployee();
        if(!idEmployee.equals(expectedIdEmployee)) throw new Exception("idEmployee doesn't match idEmployee found (idk how you received this)");

        String expectedPassword = entity.getPassword();
        if(!bCryptPasswordEncoder.matches(password, expectedPassword)) throw new Exception("Password is incorrect");

        Map<String, String> response = new HashMap<>();
        response.put("authenticated", "true");
        response.put("emplRole", entity.getEmplRole());
        response.put("idEmployee", entity.getIdEmployee());
        response.put("exception", null);

        return response;
    }

    //OPERATIONS

    public List<EmployeeInfo> findAllSortedByEmplSurname() {
        List<EmployeeInfo> result = new ArrayList<>();
        for (Employee entity : employeeRepo.findAllSortedByEmplSurname()) {
            result.add(entity.toInfo());
        }
        return result;
    }

    public List<EmployeeInfo> findCashiersSortedByEmplSurname() {
        List<EmployeeInfo> result = new ArrayList<>();
        for (Employee entity : employeeRepo.findCashiersSortedByEmplSurname()) {
            result.add(entity.toInfo());
        }
        return result;
    }

    public List<EmployeeInfo> findAllBySurname(String surname) {
        List<EmployeeInfo> result = new ArrayList<>();
        for (Employee entity : employeeRepo.findAllWithEmplSurnameContains(surname)) {
            result.add(entity.toInfo());
        }
        return result;
    }

    public List<EmployeeInfo> getCashiersWhoSoldMoreThanAverage() {
        return employeeRepo.getCashiersWhoSoldMoreThanAverage();
    }

    public List<EmployeeInfo> getCashiersWhoSoldAllProducts() {
        return employeeRepo.getCashiersWhoSoldAllProducts();
    }

    //DEFAULT OPERATIONS

    public List<EmployeeInfo> findAll() {
        List<EmployeeInfo> result = new ArrayList<>();
        for (Employee entity : employeeRepo.findAll()) {
            result.add(entity.toInfo());
        }
        return result;
    }

    public EmployeeInfo findById(String idEmployee) {
        return employeeRepo.findById(idEmployee).toInfo();
    }

    public EmployeeInfo create(EmployeeModel employeeModel) throws Exception {
        if(employeeModel.getPassword().length() > 16)
            throw new Exception("Password can't be longer than 16 symbols");

        Employee entity = employeeModel.toEntity();

        String encryptedPassword = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(encryptedPassword);

        return employeeRepo.create(entity).toInfo();
    }

    public void update(EmployeeModel employeeModel) throws Exception {
        if(employeeModel.getPassword().length() > 16)
            throw new Exception("Password can't be longer than 16 symbols");

        Employee entity = employeeModel.toEntity();

        String encryptedPassword = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(encryptedPassword);

        employeeRepo.update(entity);
    }


    public void deleteById(String idEmployee){
        employeeRepo.deleteById(idEmployee);
    }

    public void delete(@NonNull Collection<Employee> list){
        employeeRepo.delete(list);
    }

    public void deleteAll(){
        employeeRepo.deleteAll();
    }
}
