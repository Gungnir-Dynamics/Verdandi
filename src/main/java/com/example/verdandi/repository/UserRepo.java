package com.example.verdandi.repository;

import com.example.verdandi.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepo {

    private final JdbcTemplate jdbcTemplate;

    public UserRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("profile_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setHourlyRate(rs.getInt("hourly_rate"));
        user.setRole(rs.getString("role_name"));

        return user;
    };

    public List<User> getUsers() {
        String sql = """
                SELECT profile.name, 
                profile.username, 
                profile.password, 
                profile.email, 
                profile.hourly_rate
               
                FROM profile
                LEFT JOIN role
                on role.role_id = profile.role_id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }



    public User findUserByEmail(String email) {
        String sql = """
                SELECT profile_id, username, password, email
                FROM profile
                WHERE email = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email);

        } catch (Exception e) {
            return null;
        }
    }

    public User findUserById(int id) {
        String sql = """
                SELECT profile_id, username, password, email
                FROM profile
                WHERE id = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO profile (username, password, email, hourly_rate) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail());

    }

    public void editProfile(User user) {
        String sql = "UPDATE profile SET username = ?, password = ? WHERE email =?";
        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }

    public void deleteProfile(String email) {
        String sql = """
                DELETE FROM profile
                WHERE email = ?
                """;
        jdbcTemplate.update(sql, email);
    }
}