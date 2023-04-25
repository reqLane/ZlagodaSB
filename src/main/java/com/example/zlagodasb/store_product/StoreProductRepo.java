package com.example.zlagodasb.store_product;

import com.example.zlagodasb.store_product.model.StoreProductInfo;
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
public class StoreProductRepo {

    private final StoreProductRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public StoreProductRepo(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new StoreProductRowMapper();
        tableName = StoreProduct.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<StoreProduct> findByIdProduct(Integer idProduct) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_product = ?";
        return jdbcTemplate.query(sql, mapper, idProduct);
    }

    @Transactional(readOnly=true)
    public List<StoreProduct> findAllSortedByProductsNumber() {
        String sql = "SELECT * FROM " + tableName +
                " ORDER BY products_number";
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public List<StoreProductInfo> getInfoByUPC(String UPC) {
        String sql = "SELECT Store_Product.selling_price, Store_Product.products_number, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Store_Product INNER JOIN Product" +
                " ON Store_Product.id_product = Product.id_product";
        return jdbcTemplate.query(sql, new StoreProductInfoRowMapper());
    }

    @Transactional(readOnly=true)
    public List<StoreProductInfo> findAllPromotionalSortedBy(String sortBy) {
        if(!sortBy.equals("products_number") && !sortBy.equals("product_name")) sortBy = "product_name";

        String sql = "SELECT Store_Product.selling_price, Store_Product.products_number, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Store_Product INNER JOIN Product" +
                " ON Store_Product.id_product = Product.id_product" +
                " WHERE Store_Product.promotional_product = TRUE" +
                " ORDER BY " + sortBy;
        return jdbcTemplate.query(sql, new StoreProductInfoRowMapper());
    }

    @Transactional(readOnly=true)//distinct
    public List<StoreProductInfo> findAllNonPromotionalSortedBy(String sortBy) {
        if(!sortBy.equals("products_number") && !sortBy.equals("product_name")) sortBy = "product_name";

        String sql = "SELECT Store_Product.selling_price, Store_Product.products_number, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Store_Product INNER JOIN Product" +
                " ON Store_Product.id_product = Product.id_product" +
                " WHERE Store_Product.promotional_product = FALSE" +
                " ORDER BY " + sortBy;
        return jdbcTemplate.query(sql, new StoreProductInfoRowMapper());
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<StoreProduct> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public StoreProduct findById(String UPC){
        String sql = "SELECT * FROM " + tableName +
                " WHERE UPC = ?";
        List<StoreProduct> list = jdbcTemplate.query(sql, mapper, UPC);

        if(list.isEmpty()) return null;
        return list.get(0);
    }

    @Transactional
    public StoreProduct create(final StoreProduct storeProduct){
        final String sql = "INSERT INTO " + tableName +
                "(UPC, id_product, selling_price, products_number, expiration_date, promotional_product)" +
                " VALUES(?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(0, storeProduct.getUPC());
            ps.setInt(1, storeProduct.getIdProduct());
            ps.setBigDecimal(2, storeProduct.getSellingPrice());
            ps.setInt(3, storeProduct.getProductsNumber());
            ps.setDate(4, storeProduct.getExpirationDate());
            ps.setBoolean(5, storeProduct.isPromotionalProduct());
            return ps;
        }, holder);

        return storeProduct;
    }

    @Transactional
    public void update(final StoreProduct storeProduct) {
        String sql = "UPDATE " + tableName +
                " SET id_product = ?, selling_price = ?, products_number = ?, expiration_date = ?, promotional_product = ?" +
                " WHERE UPC = ?";
        jdbcTemplate.update(sql,
                storeProduct.getIdProduct(),
                storeProduct.getSellingPrice(),
                storeProduct.getProductsNumber(),
                storeProduct.getExpirationDate(),
                storeProduct.isPromotionalProduct(),
                storeProduct.getUPC());
    }

    @Transactional
    public void deleteById(String UPC){
        String sql = "DELETE FROM " + tableName +
                " WHERE UPC = ?";
        jdbcTemplate.update(sql, UPC);
    }

    @Transactional
    public void delete(@NonNull Collection<StoreProduct> list){
        for(StoreProduct entity: list)
            deleteById(entity.getUPC());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class StoreProductRowMapper implements RowMapper<StoreProduct> {
        @Override
        public StoreProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            StoreProduct storeProduct = new StoreProduct();
            storeProduct.setUPC(rs.getString("UPC"));
            storeProduct.setIdProduct(rs.getInt("id_product"));
            storeProduct.setSellingPrice(rs.getBigDecimal("selling_price"));
            storeProduct.setProductsNumber(rs.getInt("products_number"));
            storeProduct.setExpirationDate(rs.getDate("expiration_date"));
            storeProduct.setPromotionalProduct(rs.getBoolean("promotional_product"));
            return storeProduct;
        }
    }

    private static class StoreProductInfoRowMapper implements RowMapper<StoreProductInfo> {
        @Override
        public StoreProductInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            StoreProductInfo storeProductInfo = new StoreProductInfo();
            storeProductInfo.setSellingPrice(rs.getBigDecimal("selling_price"));
            storeProductInfo.setProductsNumber(rs.getInt("products_number"));
            storeProductInfo.setProductName(rs.getString("product_name"));
            storeProductInfo.setManufacturer(rs.getString("manufacturer"));
            storeProductInfo.setCharacteristics(rs.getString("characteristics"));
            return storeProductInfo;
        }
    }
}
