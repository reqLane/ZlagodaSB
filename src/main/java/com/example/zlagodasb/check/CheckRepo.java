package com.example.zlagodasb.check;

import com.example.zlagodasb.check.model.CheckInfo;
import com.example.zlagodasb.sale.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository
public class CheckRepo {

    private final CheckRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public CheckRepo(JdbcTemplate jdbcTemplate,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new CheckRowMapper();
        tableName = Check.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<Check> findByIdEmployee(String idEmployee) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_employee = ?";
        return jdbcTemplate.query(sql, mapper, idEmployee);
    }

    @Transactional(readOnly=true)
    public List<Check> findByCardNumber(String cardNumber) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE card_number = ?";
        return jdbcTemplate.query(sql, mapper, cardNumber);
    }

    @Transactional(readOnly=true)
    public List<CheckInfo> getChecksInfoOfCashierInPeriod(String idCashier, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_employee = ?" +
                " AND print_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new CheckInfoRowMapper(), idCashier, startDate, endDate);
    }

    @Transactional(readOnly=true)
    public List<CheckInfo> getAllChecksInfoInPeriod(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE print_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new CheckInfoRowMapper(), startDate, endDate);
    }

    @Transactional(readOnly=true)//distinct
    public CheckInfo getCheckInfoByCheckNumber(String checkNumber) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE check_number = ?";
        List<CheckInfo> list = jdbcTemplate.query(sql, new CheckInfoRowMapper(), checkNumber);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional(readOnly=true)
    public BigDecimal getTotalIncomeFromChecksOfCashierInPeriod(String idCashier, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT SUM(COALESCE(sum_total))" +
                " FROM " + tableName +
                " WHERE id_employee = ?" +
                " AND print_date BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, idCashier, startDate, endDate);
    }

    @Transactional(readOnly=true)
    public BigDecimal getTotalIncomeFromChecksInPeriod(Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT SUM(COALESCE(sum_total))" +
                " FROM " + tableName +
                " WHERE print_date BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, startDate, endDate);
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Check> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public Check findById(String checkNumber){
        String sql = "SELECT * FROM " + tableName +
                " WHERE check_number = ?";
        List<Check> list = jdbcTemplate.query(sql, mapper, checkNumber);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional
    public Check create(final Check check){
        final String sql = "INSERT INTO " + tableName +
                "(check_number, id_employee, card_number, print_date, sum_total, vat)" +
                " VALUES(?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, check.getCheckNumber());
            ps.setString(2, check.getIdEmployee());
            ps.setString(3, check.getCardNumber());
            ps.setTimestamp(4, check.getPrintDate());
            ps.setBigDecimal(5, check.getSumTotal());
            ps.setBigDecimal(6, check.getVat());
            return ps;
        }, holder);

        return check;
    }

    @Transactional
    public void update(final Check check) {
        String sql = "UPDATE " + tableName +
                " SET id_employee = ?, card_number = ?, print_date = ?, sum_total = ?, vat = ?" +
                " WHERE check_number = ?";
        jdbcTemplate.update(sql,
                check.getIdEmployee(),
                check.getCardNumber(),
                check.getPrintDate(),
                check.getSumTotal(),
                check.getVat(),
                check.getCheckNumber());
    }

    @Transactional
    public void deleteById(String checkNumber){
        String sql = "DELETE FROM " + tableName +
                " WHERE check_number = ?";
        jdbcTemplate.update(sql, checkNumber);
    }

    @Transactional
    public void delete(@NonNull Collection<Check> list){
        for(Check entity: list)
            deleteById(entity.getCheckNumber());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class CheckRowMapper implements RowMapper<Check> {
        @Override
        public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
            Check check = new Check();
            check.setCheckNumber(rs.getString("check_number"));
            check.setIdEmployee(rs.getString("id_employee"));
            check.setCardNumber(rs.getString("card_number"));
            check.setPrintDate(rs.getTimestamp("print_date"));
            check.setSumTotal(rs.getBigDecimal("sum_total"));
            check.setVat(rs.getBigDecimal("vat"));
            return check;
        }
    }

    private static class CheckInfoRowMapper implements RowMapper<CheckInfo> {
        @Override
        public CheckInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            CheckInfo checkInfo = new CheckInfo();
            checkInfo.setCheckNumber(rs.getString("check_number"));
            checkInfo.setIdEmployee(rs.getString("id_employee"));
            checkInfo.setCardNumber(rs.getString("card_number"));
            checkInfo.setPrintDate(rs.getTimestamp("print_date"));
            checkInfo.setSumTotal(rs.getBigDecimal("sum_total"));
            checkInfo.setVat(rs.getBigDecimal("vat"));
            return checkInfo;
        }
    }
}
