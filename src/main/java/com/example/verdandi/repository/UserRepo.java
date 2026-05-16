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
                SELECT 
                    profile.profile_id,
                    profile.username,
                    profile.password,
                    profile.email,
                    profile.hourly_rate,
                    role.role_name
                FROM profile
                LEFT JOIN role 
                    ON role.role_id = profile.role_id
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
                SELECT profile_id, username, email, password, hourly_rate, role_id
                FROM profile
                WHERE profile_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public User findUserByUsername(String username) {

        String sql = """
                SELECT profile_id, username, email, password, hourly_rate, role_id
                FROM profile
                WHERE username = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    public void saveUser(User user) {

        String sql = """
                INSERT INTO profile (username, password, email, hourly_rate, role_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getHourlyRate(),
                user.getRole()
        );
    }

    public void editProfile(User user) {
        String sql = """
                UPDATE profile 
                SET 
                    username = ?, 
                    password = ?, 
                    email = ? 
                WHERE 
                    profile_id = ?
                """;
        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getId()
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