package demo.longvh.demowebapp.service;

import demo.longvh.demowebapp.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(int id);
    List<Product> searchProducts(String keyword);
    boolean addProduct(String name, String desc, double price, String category, int stock, String imgURL);
    boolean deleteProduct(int id);
}
