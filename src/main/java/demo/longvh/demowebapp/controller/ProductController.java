package demo.longvh.demowebapp.controller;

import demo.longvh.demowebapp.models.Product;
import demo.longvh.demowebapp.service.imple.ProductServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(@RequestParam(value = "keyword", required = false)
                               String keyword, Model model) {
        if (keyword!= null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
            model.addAttribute("result",keyword);
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        return "products";
    }

    @GetMapping("product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-detail";
        }
        return "redirect/products";

    }
}
