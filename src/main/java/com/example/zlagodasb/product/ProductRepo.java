package com.example.zlagodasb.product;

import com.example.zlagodasb.product.model.ProductInfo;
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
public class ProductRepo {

    private final ProductRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public ProductRepo(JdbcTemplate jdbcTemplate,
                       DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new ProductRowMapper();
        tableName = Product.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public List<ProductInfo> findAllInfo() {
        String sql = "SELECT Product.id_product, Category.category_name, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Product INNER JOIN Category ON Product.category_number = Category.category_number";
        return jdbcTemplate.query(sql, new ProductInfoRowMapper());
    }

    @Transactional(readOnly=true)
    public List<Product> findByCategoryNumber(Integer categoryNumber) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE category_number = ?";
        return jdbcTemplate.query(sql, mapper, categoryNumber);
    }

    @Transactional(readOnly=true)
    public Product findByProductName(String productName) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE product_name = ?";
        List<Product> list = jdbcTemplate.query(sql, mapper, productName);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional(readOnly=true)
    public List<ProductInfo> findAllSortedByProductName() {
        String sql = "SELECT Product.id_product, Category.category_name, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Product INNER JOIN Category ON Product.category_number = Category.category_number" +
                " ORDER BY Product.product_name";
        return jdbcTemplate.query(sql, new ProductInfoRowMapper());
    }

    @Transactional(readOnly=true)
    public List<ProductInfo> findAllWithCategoryNumberSortedByName(Integer categoryNumber) {
        String sql = "SELECT Product.id_product, Category.category_name, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Product INNER JOIN Category ON Product.category_number = Category.category_number" +
                " WHERE Product.category_number = ?" +
                " ORDER BY Product.product_name";
        return jdbcTemplate.query(sql, new ProductInfoRowMapper(), categoryNumber);
    }

    @Transactional(readOnly=true)
    public Long getTotalAmountOfProductSoldInPeriod(Integer idProduct, Timestamp startDate, Timestamp endDate) {
        String sql = "SELECT SUM(COALESCE(Sale.product_number))" +
                " FROM Product INNER JOIN Store_Product ON Product.id_product = Store_Product.id_product" +
                " INNER JOIN Sale ON Store_Product.UPC = Sale.UPC" +
                " INNER JOIN Checks ON Sale.check_number = Checks.check_number" +
                " WHERE Product.id_product = ?" +
                " AND Checks.print_date BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, Long.class, idProduct, startDate, endDate);
    }

    @Transactional(readOnly=true)
    public List<ProductInfo> findAllByProductName(String productName) {
        String sql = "SELECT Product.id_product, Category.category_name, Product.product_name, Product.manufacturer, Product.characteristics" +
                " FROM Product INNER JOIN Category ON Product.category_number = Category.category_number" +
                " WHERE Product.product_name LIKE ?";
        return jdbcTemplate.query(sql, new ProductInfoRowMapper(), '%' + productName + '%');
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Product> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public Product findById(Integer idProduct){
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_product = ?";
        List<Product> list = jdbcTemplate.query(sql, mapper, idProduct);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional
    public Product create(final Product product){
        final String sql = "INSERT INTO " + tableName +
                "(category_number, product_name, manufacturer, characteristics)" +
                " VALUES(?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getCategoryNumber());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getManufacturer());
            ps.setString(4, product.getCharacteristics());
            return ps;
        }, holder);

        Integer idProduct = (Integer)holder.getKeyList().get(0).get("id_product");
        product.setIdProduct(idProduct);

        return product;
    }

    @Transactional
    public void update(final Product product) {
        String sql = "UPDATE " + tableName +
                " SET category_number = ?, product_name = ?, manufacturer = ?, characteristics = ?" +
                " WHERE id_product = ?";
        jdbcTemplate.update(sql,
                product.getCategoryNumber(),
                product.getProductName(),
                product.getManufacturer(),
                product.getCharacteristics(),
                product.getIdProduct());
    }

    @Transactional
    public void deleteById(Integer idProduct){
        String sql = "DELETE FROM " + tableName +
                " WHERE id_product = ?";
        jdbcTemplate.update(sql, idProduct);
    }

    @Transactional
    public void delete(@NonNull Collection<Product> list){
        for(Product entity: list)
            deleteById(entity.getIdProduct());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setIdProduct(rs.getInt("id_product"));
            product.setCategoryNumber(rs.getInt("category_number"));
            product.setProductName(rs.getString("product_name"));
            product.setManufacturer(rs.getString("manufacturer"));
            product.setCharacteristics(rs.getString("characteristics"));
            return product;
        }
    }

    private static class ProductInfoRowMapper implements RowMapper<ProductInfo> {
        @Override
        public ProductInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setIdProduct(rs.getInt("id_product"));
            productInfo.setCategoryName(rs.getString("category_name"));
            productInfo.setProductName(rs.getString("product_name"));
            productInfo.setManufacturer(rs.getString("manufacturer"));
            productInfo.setCharacteristics(rs.getString("characteristics"));
            return productInfo;
        }
    }
}
