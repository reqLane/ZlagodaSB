package com.example.zlagodasb.product;

import com.example.zlagodasb.store_product.StoreProductRepo;
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

    private final StoreProductRepo storeProductRepo;

    @Autowired
    public ProductRepo(JdbcTemplate jdbcTemplate,
                       StoreProductRepo storeProductRepo,
                       DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.storeProductRepo = storeProductRepo;
        this.dataSource = dataSource;
        mapper = new ProductRowMapper();
        tableName = Product.TABLE_NAME;
    }

    //OPERATIONS

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

        Product result = list.get(0);
        result.setStoreProducts(storeProductRepo.findByIdProduct(result.getIdProduct()));
        return result;
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Product> findAll(){
        String sql = "SELECT * FROM " + tableName;
        List<Product> result = jdbcTemplate.query(sql, mapper);

        for (Product product : result) {
            product.setStoreProducts(storeProductRepo.findByIdProduct(product.getIdProduct()));
        }

        return result;
    }

    @Transactional(readOnly=true)
    public Product findById(Integer idProduct){
        String sql = "SELECT * FROM " + tableName +
                " WHERE id_product = ?";
        List<Product> list = jdbcTemplate.query(sql, mapper, idProduct);
        if(list.isEmpty()) return null;

        Product result = list.get(0);
        result.setStoreProducts(storeProductRepo.findByIdProduct(result.getIdProduct()));
        return result;
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
}
