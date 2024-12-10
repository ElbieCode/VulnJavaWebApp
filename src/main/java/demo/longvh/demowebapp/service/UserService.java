package demo.longvh.demowebapp.service;


import demo.longvh.demowebapp.models.User;

import java.util.List;

public interface UserService {
    int registerUser(User user);
    int addUser(String username, String password, String fullName, String address, String email,String role, String profileImgURL);
    boolean loginUser(String username, String password);
    boolean deleteUser(String username);
    void updateUserDetails(String username, String fullName,String address, String email, String profileImgURL);
    User findById(Integer id);
    User findByUsername(String username);
    List<User> getAllUsers();
    List<User> searchForUser(String username);
}
