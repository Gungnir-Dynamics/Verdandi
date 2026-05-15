package com.example.verdandi.repository;

import com.example.verdandi.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
        user.setRoleId(rs.getInt("role_id"));

        return user;
    };

    public User findUserByEmail(String email) {
        String sql = """
                SELECT profile_id, username, password, email
                FROM Profile
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
                FROM Profile
                WHERE id = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO Profile (username, password, email, hourly_rate) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail());

    }

    public void editProfile(User user) {
        String sql = "UPDATE Profile SET username = ?, password = ? WHERE email =?";
        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
    }

    public void deleteProfile(String email) {
        String sql = """
                DELETE FROM Profile
                WHERE email = ?
                """;
        jdbcTemplate.update(sql, email);
    }
}