package com.example.zlagodasb.employee;

import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.employee.model.EmployeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
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
