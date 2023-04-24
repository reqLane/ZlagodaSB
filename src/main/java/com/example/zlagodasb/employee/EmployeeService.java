package com.example.zlagodasb.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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



    //DEFAULT OPERATIONS

    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public Employee findById(String idEmployee) {
        return employeeRepo.findById(idEmployee);
    }

    public Employee create(EmployeeModel employeeModel) {
        return employeeRepo.create(employeeModel.toEntity());
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
