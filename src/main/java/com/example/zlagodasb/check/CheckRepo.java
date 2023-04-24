package com.example.zlagodasb.check;

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
import java.sql.*;
import java.util.Collection;
import java.util.List;

@Repository
public class CheckRepo {

    private final CheckRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    private final SaleRepo saleRepo;

    @Autowired
    public CheckRepo(JdbcTemplate jdbcTemplate,
                        SaleRepo saleRepo,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.saleRepo = saleRepo;
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

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Check> findAll(){
        String sql = "SELECT * FROM " + tableName;
        List<Check> result = jdbcTemplate.query(sql, mapper);

        for (Check check : result)
            check.setSales(saleRepo.findByCheckNumber(check.getCheckNumber()));

        return result;
    }

    @Transactional(readOnly=true)
    public Check findById(String checkNumber){
        String sql = "SELECT * FROM " + tableName +
                " WHERE check_number = ?";
        List<Check> list = jdbcTemplate.query(sql, mapper, checkNumber);
        if(list.isEmpty()) return null;

        Check result = list.get(0);
        result.setSales(saleRepo.findByCheckNumber(result.getCheckNumber()));
        return result;
    }

    @Transactional
    public Check create(final Check check){
        final String sql = "INSERT INTO " + tableName +
                "(check_number, id_employee, card_number, print_date, sum_total, vat)" +
                " VALUES(?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(0, check.getCheckNumber());
            ps.setString(1, check.getIdEmployee());
            ps.setString(2, check.getCardNumber());
            ps.setDate(3, check.getPrintDate());
            ps.setBigDecimal(4, check.getSumTotal());
            ps.setBigDecimal(5, check.getVat());
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
            check.setPrintDate(rs.getDate("print_date"));
            check.setSumTotal(rs.getBigDecimal("sum_total"));
            check.setVat(rs.getBigDecimal("vat"));
            return check;
        }
    }
}