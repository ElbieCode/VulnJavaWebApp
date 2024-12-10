package demo.longvh.demowebapp.repository;

import demo.longvh.demowebapp.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAllProduct() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public Product findProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = " + id;
        try {
            return jdbcTemplate.queryForObject(sql, new ProductRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Product> findProductByKeyword(String keyword) {
        String sql = "SELECT * FROM products WHERE productName LIKE '%" + keyword + "%' "+"OR productDescription like '%" + keyword + "%'";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public int deleteProductById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql,id);
    }

    public int addProduct(String name, double price, String description,
                          String category, int stock, String imgURL) {
        String sql = "INSERT INTO products (productName, productDescription," +
                " productPrice, productCategory, productStock, productImageURL) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, name, description, price, category, stock, imgURL);
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setProductName(rs.getString("productName"));
            product.setProductDescription(rs.getString("productDescription"));
            product.setProductPrice(rs.getDouble("productPrice"));
            product.setProductCategory(rs.getString("productCategory"));
            product.setProductStock(rs.getInt("productStock"));
            product.setProductImageURL(rs.getString("productImageURL"));
            return product;
        }
    }
}
