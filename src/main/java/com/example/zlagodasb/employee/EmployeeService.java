package com.example.zlagodasb.employee;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepo employeeRepo;

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public Employee findById(String idEmployee) {
        return employeeRepo.findById(idEmployee);
    }

    public Employee create(Employee employee) {
        return employeeRepo.create(employee);
    }

    public void update(Employee employee) {
        employeeRepo.update(employee);
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
