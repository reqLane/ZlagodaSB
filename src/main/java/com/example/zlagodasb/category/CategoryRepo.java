package com.example.zlagodasb.category;

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
public class CategoryRepo {

    private final CategoryRowMapper mapper;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final DataSource dataSource;

    @Autowired
    public CategoryRepo(JdbcTemplate jdbcTemplate,
                        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        mapper = new CategoryRowMapper();
        tableName = Category.TABLE_NAME;
    }

    //OPERATIONS

    @Transactional(readOnly=true)
    public Category findByCategoryName(String categoryName) {
        String sql = "SELECT * FROM " + tableName +
                " WHERE category_name = ?";
        List<Category> list = jdbcTemplate.query(sql, mapper, categoryName);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional(readOnly=true)
    public List<Category> findAllSortedByCategoryName() {
        String sql = "SELECT * FROM " + tableName +
                " ORDER BY category_name";
        return jdbcTemplate.query(sql, mapper);
    }

    //DEFAULT OPERATIONS

    @Transactional(readOnly=true)
    public List<Category> findAll(){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    @Transactional(readOnly=true)
    public Category findById(Integer categoryNumber){
        String sql = "SELECT * FROM " + tableName +
                " WHERE category_number = ?";
        List<Category> list = jdbcTemplate.query(sql, mapper, categoryNumber);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    @Transactional
    public Category create(final Category category){
        final String sql = "INSERT INTO " + tableName +
                "(category_name)" +
                " VALUES(?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategoryName());
            return ps;
        }, holder);

        Integer categoryNumber = (Integer)holder.getKeyList().get(0).get("category_number");
        category.setCategoryNumber(categoryNumber);

        return category;
    }

    @Transactional
    public void update(final Category category) {
        String sql = "UPDATE " + tableName +
                " SET category_name = ?" +
                " WHERE category_number = ?";
        jdbcTemplate.update(sql,
                category.getCategoryName(),
                category.getCategoryNumber());
    }

    @Transactional
    public void deleteById(Integer categoryNumber){
        String sql = "DELETE FROM " + tableName +
                " WHERE category_number = ?";
        jdbcTemplate.update(sql, categoryNumber);
    }

    @Transactional
    public void delete(@NonNull Collection<Category> list){
        for(Category entity: list)
            deleteById(entity.getCategoryNumber());
    }

    @Transactional
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    //MAPPER

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setCategoryNumber(rs.getInt("category_number"));
            category.setCategoryName(rs.getString("category_name"));
            return category;
        }
    }
}
