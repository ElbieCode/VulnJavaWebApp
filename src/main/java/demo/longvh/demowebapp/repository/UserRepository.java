package demo.longvh.demowebapp.repository;

import demo.longvh.demowebapp.models.User;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(User user) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//
//                String sql = "INSERT INTO users (username, password, fullName, email, address, role) VALUES (?,?,?,?,?,?)";
//        return jdbcTemplate.update(sql, user.getUsername(),user.getPassword(),
//                user.getFullName(), user.getEmail(), user.getAddress(), "USER");

        String sql = "INSERT INTO users (username, password, fullName," +
                " email, address, role, profileImgURL) VALUES ('"
                + user.getUsername() + "', '"
                + user.getPassword() + "', '"
                + user.getFullName() + "', '"
                + user.getEmail() + "', '"
                + user.getAddress() + "', '"
                + "USER', 'default.png')";
        return jdbcTemplate.update(sql);
    }

    public User findByUserName(String username) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper());
    }

    public User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = '" + id + "'";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper());
    }

    public boolean authen(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        try {
//            String sql = "SELECT password FROM users WHERE username = ?";
//            List<String> pass = jdbcTemplate.queryForList(sql, String.class, username);
//            if (pass.isEmpty()) {
//                return false;
//            }
//            String hashedPass = pass.get(0);
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            return encoder.matches(password, hashedPass);

            String sql = "SELECT * FROM users WHERE USERNAME= '" + username +
                         "' AND PASSWORD='" + password +"'";

//            String sql = "SELECT * FROM users WHERE USERNAME= ? AND PASSWORD=?";
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
            return !users.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update(String username, String fullName, String address, String email, String profileImgURL) {
//        String sql = "UPDATE users SET fullName = ?, address =?, email = ?, profileImgURL = ? WHERE username = ?";
//        jdbcTemplate.update(sql,fullName,address,email,profileImgURL,username);

        String sql = "UPDATE users SET fullName = '" + fullName +
                "', address ='" + address +
                "', email ='" + email +
                "', profileImgURL ='" + profileImgURL +
                "' WHERE username = '" + username + "'";
        jdbcTemplate.update(sql);
    }

    public List<User> findByKeyword(String keyword) {
        String sql = "SELECT * FROM users WHERE username LIKE ? OR fullName LIKE ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), "%" + keyword + "%", "%" + keyword + "%");

    }

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public int deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    public int addUser(String username, String password, String fullName, String address, String email, String role, String profileImgURL) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);

        String sql = "INSERT INTO users (username, password, fullName, email, address, role, profileImgURL) VALUES (?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, username, password,
                fullName, email, address, role, profileImgURL);

    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setFullName(rs.getString("fullName"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setRole(rs.getString("role"));
            user.setProfileImgURL(rs.getString("profileImgURL"));
            return user;
        }
    }

}
