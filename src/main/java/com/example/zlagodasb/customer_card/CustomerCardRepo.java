package com.example.zlagodasb.customer_card;

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
public class CustomerCardRepo {

    private final CustomerCardRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public CustomerCardRepo(JdbcTemplate jdbcTemplate,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new CustomerCardRowMapper();
        tableName = CustomerCard.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<CustomerCard> findAllSortedByCustSurname() {
        String sql = "SELECT * FROM " + tableName +
                " ORDER BY cust_surname";
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public List<CustomerCard> findAllWithPercentSortedBySurname(Integer percent) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE percent = ?" +
                " ORDER BY cust_surname";
        return jdbcTemplate.query(sql, mapper, percent);
    }

    @Transactional(readOnly=true)
    public List<CustomerCard> findAllByCustSurname(String custSurname) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE cust_surname LIKE ?";
        return jdbcTemplate.query(sql, mapper, '%' + custSurname + '%');
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<CustomerCard> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public CustomerCard findById(String cardNumber){
        String sql = "SELECT * FROM " + tableName +
                " WHERE card_number = ?";
        List<CustomerCard> list = jdbcTemplate.query(sql, mapper, cardNumber);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional
    public CustomerCard create(final CustomerCard customerCard){
        final String sql = "INSERT INTO " + tableName +
                "(card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customerCard.getCardNumber());
            ps.setString(2, customerCard.getCustSurname());
            ps.setString(3, customerCard.getCustName());
            ps.setString(4, customerCard.getCustPatronymic());
            ps.setString(5, customerCard.getPhoneNumber());
            ps.setString(6, customerCard.getCity());
            ps.setString(7, customerCard.getStreet());
            ps.setString(8, customerCard.getZipCode());
            ps.setInt(9, customerCard.getPercent());
            return ps;
        }, holder);

        return customerCard;
    }

    @Transactional
    public void update(final CustomerCard customerCard) {
        String sql = "UPDATE " + tableName +
                " SET cust_surname = ?, cust_name = ?, cust_patronymic = ?, phone_number = ?, city = ?, street = ?, zip_code = ?, percent = ?" +
                " WHERE card_number = ?";
        jdbcTemplate.update(sql,
                customerCard.getCustSurname(),
                customerCard.getCustName(),
                customerCard.getCustPatronymic(),
                customerCard.getPhoneNumber(),
                customerCard.getCity(),
                customerCard.getStreet(),
                customerCard.getZipCode(),
                customerCard.getPercent(),
                customerCard.getCardNumber());
    }

    @Transactional
    public void deleteById(String cardNumber){
        String sql = "DELETE FROM " + tableName +
                " WHERE card_number = ?";
        jdbcTemplate.update(sql, cardNumber);
    }

    @Transactional
    public void delete(@NonNull Collection<CustomerCard> list){
        for(CustomerCard entity: list)
            deleteById(entity.getCardNumber());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class CustomerCardRowMapper implements RowMapper<CustomerCard> {
        @Override
        public CustomerCard mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerCard customerCard = new CustomerCard();
            customerCard.setCardNumber(rs.getString("card_number"));
            customerCard.setCustSurname(rs.getString("cust_surname"));
            customerCard.setCustName(rs.getString("cust_name"));
            customerCard.setCustPatronymic(rs.getString("cust_patronymic"));
            customerCard.setPhoneNumber(rs.getString("phone_number"));
            customerCard.setCity(rs.getString("city"));
            customerCard.setStreet(rs.getString("street"));
            customerCard.setZipCode(rs.getString("zip_code"));
            customerCard.setPercent(rs.getInt("percent"));
            return customerCard;
        }
    }
}
