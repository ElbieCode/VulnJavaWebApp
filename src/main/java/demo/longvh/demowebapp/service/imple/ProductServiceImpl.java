package demo.longvh.demowebapp.service.imple;

import demo.longvh.demowebapp.models.Product;
import demo.longvh.demowebapp.repository.ProductRepository;
import demo.longvh.demowebapp.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllProduct();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findProductById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findProductByKeyword(keyword);
    }

    @Override
    public boolean addProduct(String name, String desc, double price, String category, int stock, String imgURL) {
        return productRepository.addProduct(name, price, desc, category, stock,imgURL) > 0;
    }

    @Override
    public boolean deleteProduct(int id) {
        return productRepository.deleteProductById(id) > 0;
    }

}
