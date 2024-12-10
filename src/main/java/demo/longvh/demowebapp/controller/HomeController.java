/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package demo.longvh.demowebapp.controller;

import demo.longvh.demowebapp.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Acer
 */
@Controller
public class HomeController {
    @RequestMapping(value = {"/","home","index"})
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            String role = user.getRole();
            session.setAttribute("role",role);
            if (role.equals("ADMIN")) {
                return "admin/home";
            } else {
                session.setAttribute("username",user.getUsername());
                return "home_user";
            }
        } else {
            return "home_guest";
        }
    }

}
