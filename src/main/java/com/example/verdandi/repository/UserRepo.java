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
                SELECT 
                    p.profile_id, 
                    p.username, 
                    p.password, 
                    p.email, 
                    p.hourly_rate, 
                    r.role_name
                FROM 
                    profile p
                LEFT JOIN
                    role r
                ON
                    r.role_id = p.role_id
                
                WHERE p.email = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email);

        } catch (Exception e) {
            return null;
        }
    }

    public User findUserById(int id) {

        String sql = """
                SELECT 
                    p.profile_id, 
                    p.username, 
                    p.email, 
                    p.password, 
                    p.hourly_rate, 
                    r.role_name
                FROM 
                    profile p
                LEFT JOIN
                    role r
                ON
                    r.role_id = p.role_id
                WHERE 
                    p.profile_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }


    public int findRoleIdByName(String roleName) {
        String sql = """
                SELECT 
                    r.role_id 
                FROM 
                    role r
                WHERE 
                    r.role_name = ?
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, roleName);
    }

    public void saveUser(User user) {

        String sql = """
                INSERT INTO profile 
                    (username, 
                     password, 
                     email, 
                     hourly_rate, 
                     role_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        int roleId = findRoleIdByName(user.getRole());

        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getHourlyRate(),
                roleId);

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