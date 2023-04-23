package com.example.zlagodasb.sale;

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
public class SaleRepo {

    private final SaleRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public SaleRepo(JdbcTemplate jdbcTemplate,
                     DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new SaleRowMapper();
        tableName = Sale.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<Sale> findByCheckNumber(String checkNumber) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE check_number = ?";
        return jdbcTemplate.query(sql, mapper, checkNumber);
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Sale> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public Sale findById(String UPC, String checkNumber){
        String sql = "SELECT * FROM " + tableName +
                " WHERE UPC = ?" +
                " AND check_number = ?";
        List<Sale> list = jdbcTemplate.query(sql, mapper, UPC, checkNumber);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional
    public Sale create(final Sale sale){
        final String sql = "INSERT INTO " + tableName +
                "(UPC, check_number, product_number, selling_price)" +
                " VALUES(?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(0, sale.getUPC());
            ps.setString(1, sale.getCheckNumber());
            ps.setInt(2, sale.getProductNumber());
            ps.setBigDecimal(3, sale.getSellingPrice());
            return ps;
        }, holder);

        return sale;
    }

    @Transactional
    public void update(final Sale check) {
        String sql = "UPDATE " + tableName +
                " SET product_number = ?, selling_price = ?" +
                " WHERE UPC = ?" +
                " AND check_number = ?";
        jdbcTemplate.update(sql,
                check.getProductNumber(),
                check.getSellingPrice(),
                check.getUPC(),
                check.getCheckNumber());
    }

    @Transactional
    public void deleteById(String UPC, String checkNumber){
        String sql = "DELETE FROM " + tableName +
                " WHERE UPC = ?" +
                " AND check_number = ?";
        jdbcTemplate.update(sql, UPC, checkNumber);
    }

    @Transactional
    public void delete(@NonNull Collection<Sale> list){
        for(Sale entity: list)
            deleteById(entity.getUPC(), entity.getCheckNumber());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class SaleRowMapper implements RowMapper<Sale> {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale sale = new Sale();
            sale.setUPC(rs.getString("UPC"));
            sale.setCheckNumber(rs.getString("check_number"));
            sale.setProductNumber(rs.getInt("product_number"));
            sale.setSellingPrice(rs.getBigDecimal("selling_price"));
            return sale;
        }
    }
}
