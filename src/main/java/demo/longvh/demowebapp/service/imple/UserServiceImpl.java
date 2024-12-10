package demo.longvh.demowebapp.service.imple;

import demo.longvh.demowebapp.models.User;
import demo.longvh.demowebapp.repository.UserRepository;
import demo.longvh.demowebapp.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public int registerUser(User user){
        return userRepository.save(user);
    }

    @Override
    public int addUser(String username, String password, String fullName, String address, String email, String role, String profileImgURL) {
        return userRepository.addUser(username,password,fullName,address,email,role,profileImgURL);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public boolean loginUser(String username, String password) {
        return userRepository.authen(username, password);
    }

    @Override
    public boolean deleteUser(String username) {
        return userRepository.deleteUser(username) > 0;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> searchForUser(String keyword) {
        return userRepository.findByKeyword(keyword);
    }

    @Override
    public void updateUserDetails(String username, String fullName,String address, String email, String profileImgURL) {
        userRepository.update(username,fullName,address,email,profileImgURL);
    }


}
