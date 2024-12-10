package demo.longvh.demowebapp.controller.admin;

import demo.longvh.demowebapp.models.User;
import demo.longvh.demowebapp.service.imple.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        userService.registerUser(user);
        return "redirect:/done_register";
    }

    ;

    @RequestMapping("/done_register")
    public String doneRegister() {
        return "done_register";
    }

    @GetMapping("/login")
    public String getLoginForm(Model model) {
        return "login";
    }

    ;

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password,
                            Model model, HttpSession session) {

        if (userService.loginUser(username, password)) {
            User user = userService.findByUsername(username);
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }


    @GetMapping("/profile")
    public String getUserProfile(HttpSession httpSession, Model model) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/profile/edit")
    public String editProfile(HttpSession httpSession, Model model) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "profile-update";
        } else {
            return "redirect:/login";
        }
    }

    private boolean checkImgExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedTypes = Arrays.asList("jpg", "jpeg", "png", "gif");
        return allowedTypes.contains(extension);
    }


    private boolean checkImgPath(String filename) {
        Path path = Paths.get(filename).normalize();
        return !path.isAbsolute() && !path.toString().contains("..");
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) MultipartFile profilePic,
                                HttpSession httpSession,
                                RedirectAttributes redirectAttributes
    ) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            User currentUser = userService.findByUsername(loggedInUser.getUsername());

            String updatedFullName = (fullName == null || fullName.isBlank()) ? currentUser.getFullName() : fullName;
            String updatedEmail = (email == null || email.isBlank()) ? currentUser.getEmail() : email;
            String updatedAddress = (address == null || address.isBlank()) ? currentUser.getAddress() : address;

            String updatedProfileImgURL = currentUser.getProfileImgURL();

            if (!profilePic.isEmpty()) {
                try {
                    String uploadDir = "uploads/profileImg/";
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

//                    String filename = profilePic.getOriginalFilename();
//                    String extension = filename.substring(filename.lastIndexOf("."));
//                    String basename = filename.substring(0,filename.lastIndexOf("."));
//                    updatedProfileImgURL = basename + "_" + System.currentTimeMillis() + extension;

                    String fileName = profilePic.getOriginalFilename();

                    if (!checkImgExtension(fileName)) {
                        redirectAttributes.addFlashAttribute("message", "Invalid File Type");
                        return "redirect:/profile/edit";
                    }

                    if (!checkImgPath(fileName)) {
                        redirectAttributes.addFlashAttribute("message", "Invalid File Name");
                        return "redirect:/profile/edit";
                    }

                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(profilePic.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    updatedProfileImgURL = "/uploads/profileImg/" + fileName;

                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("message", "File upload failed");
                    e.printStackTrace();
                    return "redirect:/profile/edit";
                }
            }

            userService.updateUserDetails(loggedInUser.getUsername(),
                    updatedFullName, updatedAddress, updatedEmail, updatedProfileImgURL);

            User updatedUser = userService.findByUsername(loggedInUser.getUsername());

            httpSession.setAttribute("loggedInUser", updatedUser);
            return "redirect:/profile";
        }
        return "redirect:/login";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
