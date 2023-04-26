package com.example.zlagodasb.employee;

import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.employee.model.EmployeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    //AUTHENTICATION

    public Map<String, String> authenticate(String idEmployee, String password) throws Exception {
        Employee entity = employeeRepo.findById(idEmployee);
        if(entity == null) throw new Exception("Employee with idEmployee not found");

        String expectedIdEmployee = entity.getIdEmployee();
        if(!idEmployee.equals(expectedIdEmployee)) throw new Exception("idEmployee doesn't match idEmployee found (idk how you received this)");

        String expectedPassword = entity.getPassword();
        if(!password.equals(expectedPassword)) throw new Exception("Password is incorrect");

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

    public EmployeeInfo create(EmployeeModel employeeModel) {
        return employeeRepo.create(employeeModel.toEntity()).toInfo();
    }

    public void update(EmployeeModel employeeModel) {
        employeeRepo.update(employeeModel.toEntity());
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
