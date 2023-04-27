package com.example.zlagodasb.employee;

import com.example.zlagodasb.employee.model.EmployeeInfo;
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

    @Autowired
    public EmployeeRepo(JdbcTemplate jdbcTemplate,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new EmployeeRowMapper();
        tableName = Employee.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<Employee> findAllSortedByEmplSurname() {
        String sql = "SELECT * FROM " + tableName +
                " ORDER BY empl_surname";
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public List<Employee> findCashiersSortedByEmplSurname() {
        String sql = "SELECT * FROM " + tableName +
                " WHERE empl_role = 'Cashier'" +
                " ORDER BY empl_surname";
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public List<Employee> findAllWithEmplSurnameContains(String surname) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE empl_surname LIKE ?";
        return jdbcTemplate.query(sql, mapper, "%" + surname + "%");
    }

    @Transactional(readOnly=true)
    public List<EmployeeInfo> getCashiersWhoSoldMoreThanAverage() {
        String sql = "SELECT EM.id_employee, EM.empl_surname, EM.empl_name, EM.empl_patronymic, EM.empl_role," +
                " EM.salary, EM.date_of_birth, EM.date_of_start, EM.phone_number, EM.city, EM.street, EM.zip_code, SUM(CS.sum_total) AS sold_total" +
                " FROM Employee EM INNER JOIN Checks CS ON EM.id_employee = CS.id_employee" +
                " WHERE EM.empl_role = 'Cashier'" +
                " GROUP BY EM.id_employee" +
                " HAVING SUM(CS.sum_total) > (SELECT AVG(sold_total) AS sold_avg" +
                " FROM (SELECT SUM(sum_total) AS sold_total" +
                " FROM Checks" +
                " GROUP BY id_employee))";
        return jdbcTemplate.query(sql, new EmployeeInfoRowMapper());
    }

    @Transactional(readOnly=true)
    public List<EmployeeInfo> getCashiersWhoSoldAllProducts() {
        String sql = "SELECT EM.id_employee, EM.empl_surname, EM.empl_name, EM.empl_patronymic, EM.empl_role," +
                " EM.salary, EM.date_of_birth, EM.date_of_start, EM.phone_number, EM.city, EM.street, EM.zip_code" +
                " FROM Employee EM" +
                " WHERE NOT EXISTS (SELECT *" +
                " FROM Product P" +
                " WHERE NOT EXISTS (SELECT *" +
                " FROM Product PR INNER JOIN Store_Product SP ON PR.id_product = SP.id_product" +
                " INNER JOIN Sale SL ON SL.UPC = SP.UPC" +
                " INNER JOIN Checks CK ON CK.check_number = SL.check_number" +
                " INNER JOIN Employee EMP ON EMP.id_employee = CK.id_employee" +
                " WHERE P.id_product = PR.id_product" +
                " AND EM.id_employee = EMP.id_employee" +
                " AND EM.empl_role = 'Cashier'))";
        return jdbcTemplate.query(sql, new EmployeeInfoRowMapper());
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Employee> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public Employee findById(String idEmployee) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_employee = ?";
        List<Employee> list = jdbcTemplate.query(sql, mapper, idEmployee);
        if(list.isEmpty()) return null;

        return list.get(0);
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

    private static class EmployeeInfoRowMapper implements RowMapper<EmployeeInfo> {
        @Override
        public EmployeeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeInfo employeeInfo = new EmployeeInfo();
            employeeInfo.setIdEmployee(rs.getString("id_employee"));
            employeeInfo.setEmplFullName(rs.getString("empl_surname") + " " +
                    rs.getString("empl_name") +
                    (rs.getString("empl_patronymic") != null ? (" " + rs.getString("empl_patronymic")) : ""));
            employeeInfo.setEmplRole(rs.getString("empl_role"));
            employeeInfo.setSalary(rs.getBigDecimal("salary"));
            employeeInfo.setDateOfBirth(rs.getDate("date_of_birth"));
            employeeInfo.setDateOfStart(rs.getDate("date_of_start"));
            employeeInfo.setPhoneNumber(rs.getString("phone_number"));
            employeeInfo.setCity(rs.getString("city"));
            employeeInfo.setStreet(rs.getString("street"));
            employeeInfo.setZipCode(rs.getString("zip_code"));
            employeeInfo.setSoldTotal(rs.getBigDecimal("sold_total"));
            return employeeInfo;
        }
    }
}
