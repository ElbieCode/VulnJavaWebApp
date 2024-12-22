/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.longvh.demowebapp.controller.admin;

import demo.longvh.demowebapp.models.Product;
import demo.longvh.demowebapp.models.User;
import demo.longvh.demowebapp.service.imple.ProductServiceImpl;
import demo.longvh.demowebapp.service.imple.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Acer
 */
@Controller
public class AdminController {
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public AdminController(ProductServiceImpl productService, UserServiceImpl userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/admin", "admin/home"})
    public String admin(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        ;
        return "admin/home";
    }

    @GetMapping("/admin/products")
    public String showProducts(@RequestParam(value = "keyword", required = false)
                               String keyword, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(keyword));
            model.addAttribute("result", keyword);
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        return "admin/products";
    }

    @GetMapping("/admin/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "/admin/product-detail";
        }
        return "/admin/products";

    }

    @GetMapping("/admin/delete-product/{id}")
    public String deleteProduct(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            model.addAttribute("message", "Product with id: " + id + " deleted");
        } else {
            model.addAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/add-product")
    public String showAddProduct(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    @PostMapping("/admin/add-product")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("description") String desc,
                             @RequestParam("price") double price,
                             @RequestParam("category") String category,
                             @RequestParam("stock") int stock,
                             @RequestParam("image") MultipartFile img,
                             HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        String uploadDir = "uploads/productImg/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = img.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imgUrl = "/uploads/productImg/" + fileName;
        productService.addProduct(name, desc, price, category, stock, imgUrl);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/users")
    public String showUsers(@RequestParam(value = "keyword", required = false)
                            String keyword, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("users", userService.searchForUser(keyword));
            model.addAttribute("result", keyword);
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }

        return "admin/users";
    }

    @GetMapping("/admin/user/{id}")
    public String showUserDetails(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }
        User userDetails = userService.findById(id);
        if (userDetails != null) {
            model.addAttribute("userDetails", userDetails);
            return "/admin/user-detail";
        }
        return "/admin/products";

    }

    @GetMapping("/admin/delete-user/{username}")
    public String deleteUser(@PathVariable String username, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        boolean isDeleted = userService.deleteUser(username);

        if (isDeleted) {
            model.addAttribute("message", "User with id: " + username + " deleted");
        } else {
            model.addAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/add-user")
    public String showAddUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    @PostMapping("/admin/add-user")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("fullName") String fullName,
                          @RequestParam("address") String address,
                          @RequestParam("email") String email,
                          @RequestParam("role") String role,
                          @RequestParam("image") MultipartFile img,
                          HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
//        if (user == null) {
//            return "redirect:/";
//        }
//        if (!user.getRole().equals("ADMIN")) {
//            return "redirect:/";
//        }

        String uploadDir = "uploads/productImg/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = img.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imgUrl = "/uploads/productImg/" + fileName;

        userService.addUser(username,password,fullName,address,email,role,imgUrl);

        return "redirect:/admin/users";
    }
}
