package com.example.zlagodasb.employee;

import com.example.zlagodasb.check.CheckRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository
public class EmployeeRepo {

    private final EmployeeRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    private final CheckRepo checkRepo;

    @Autowired
    public EmployeeRepo(JdbcTemplate jdbcTemplate,
                        CheckRepo checkRepo,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.checkRepo = checkRepo;
        this.dataSource = dataSource;
        mapper = new EmployeeRowMapper();
        tableName = Employee.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<Employee> findAllSortedByEmplSurname() {
        String sql = "SELECT * FROM " + tableName +
                " ORDER BY empl_surname";
        List<Employee> result = jdbcTemplate.query(sql, mapper);

        for (Employee employee : result)
            employee.setChecks(checkRepo.findByIdEmployee(employee.getIdEmployee()));

        return result;
    }

    @Transactional(readOnly=true)
    public List<Employee> findCashiersSortedByEmplSurname() {
        String sql = "SELECT * FROM " + tableName +
                " WHERE empl_role = 'Cashier'" +
                " ORDER BY empl_surname";
        List<Employee> result = jdbcTemplate.query(sql, mapper);

        for (Employee employee : result)
            employee.setChecks(checkRepo.findByIdEmployee(employee.getIdEmployee()));

        return result;
    }

    @Transactional(readOnly=true)
    public List<Employee> findAllWithEmplSurnameContains(String surname) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE empl_surname LIKE ?";
        List<Employee> result = jdbcTemplate.query(sql, mapper, '%' + surname + '%');

        for (Employee employee : result)
            employee.setChecks(checkRepo.findByIdEmployee(employee.getIdEmployee()));

        return result;
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Employee> findAll() {
        String sql = "SELECT * FROM " + tableName;
        List<Employee> result = jdbcTemplate.query(sql, mapper);

        for (Employee employee : result)
            employee.setChecks(checkRepo.findByIdEmployee(employee.getIdEmployee()));

        return result;
    }

    @Transactional(readOnly=true)
    public Employee findById(String idEmployee) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_employee = ?";
        List<Employee> list = jdbcTemplate.query(sql, mapper, idEmployee);
        if(list.isEmpty()) return null;

        Employee result = list.get(0);
        result.setChecks(checkRepo.findByIdEmployee(result.getIdEmployee()));
        return result;
    }

    @Transactional
    public Employee create(final Employee employee) {
        final String sql = "INSERT INTO " + tableName +
                "(id_employee, password, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if(!employee.getEmplRole().equals("Manager") && !employee.getEmplRole().equals("Cashier"))
            employee.setEmplRole("Cashier");

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getIdEmployee());
            ps.setString(2, employee.getPassword());
            ps.setString(3, employee.getEmplSurname());
            ps.setString(4, employee.getEmplName());
            ps.setString(5, employee.getEmplPatronymic());
            ps.setString(6, employee.getEmplRole());
            ps.setBigDecimal(7, employee.getSalary());
            ps.setDate(8, employee.getDateOfBirth());
            ps.setDate(9, employee.getDateOfStart());
            ps.setString(10, employee.getPhoneNumber());
            ps.setString(11, employee.getCity());
            ps.setString(12, employee.getStreet());
            ps.setString(13, employee.getZipCode());
            return ps;
        }, holder);

        return employee;
    }

    @Transactional
    public void update(final Employee employee) {
        String sql = "UPDATE " + tableName +
                " SET password = ?, empl_surname = ?, empl_name = ?, empl_patronymic = ?, empl_role = ?, salary = ?, date_of_birth = ?, date_of_start = ?, phone_number = ?, city = ?, street = ?, zip_code = ?" +
                " WHERE id_employee = ?";
        jdbcTemplate.update(sql,
                employee.getPassword(),
                employee.getEmplSurname(),
                employee.getEmplName(),
                employee.getEmplPatronymic(),
                employee.getEmplRole(),
                employee.getSalary(),
                employee.getDateOfBirth(),
                employee.getDateOfStart(),
                employee.getPhoneNumber(),
                employee.getCity(),
                employee.getStreet(),
                employee.getZipCode(),
                employee.getIdEmployee());
    }

    @Transactional
    public void deleteById(String idEmployee) {
        String sql = "DELETE FROM " + tableName +
                " WHERE id_employee = ?";
        jdbcTemplate.update(sql, idEmployee);
    }

    @Transactional
    public void delete(@NonNull Collection<Employee> list) {
        for(Employee entity: list)
            deleteById(entity.getIdEmployee());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setIdEmployee(rs.getString("id_employee"));
            employee.setPassword(rs.getString("password"));
            employee.setEmplSurname(rs.getString("empl_surname"));
            employee.setEmplName(rs.getString("empl_name"));
            employee.setEmplPatronymic(rs.getString("empl_patronymic"));
            employee.setEmplRole(rs.getString("empl_role"));
            employee.setSalary(rs.getBigDecimal("salary"));
            employee.setDateOfBirth(rs.getDate("date_of_birth"));
            employee.setDateOfStart(rs.getDate("date_of_start"));
            employee.setPhoneNumber(rs.getString("phone_number"));
            employee.setCity(rs.getString("city"));
            employee.setStreet(rs.getString("street"));
            employee.setZipCode(rs.getString("zip_code"));
            return employee;
        }
    }
}
