package com.example.verdandi.repository;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Role;
import com.example.verdandi.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
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
        Role role = new Role();

        user.setId(rs.getInt("profile_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setHourlyRate(rs.getInt("hourly_rate"));

        role.setRoleName(rs.getString("role_name"));
        role.setId(rs.getInt("role_id"));

        user.setRole(role);

        return user;
    };


    public List<Role> getRoles() {
        String sql = """
                SELECT 
                    role_id,
                    role_name
                FROM
                    role
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Role role = new Role();
            role.setId(rs.getInt("role_id"));
            role.setRoleName(rs.getString("role_name"));

            return role;
        });
    }


    public List<User> getUsers() {

        String sql = """
                SELECT 
                    profile.profile_id,
                    profile.username,
                    profile.password,
                    profile.email,
                    profile.hourly_rate,
                    role.role_id,
                    role.role_name
                FROM profile
                LEFT JOIN role 
                    ON role.role_id = profile.role_id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<User> getUsersForProject(int projectId) {

        String sql = """
                SELECT
                    profile.profile_id,
                    profile.username,
                    profile.password,
                    profile.email,
                    profile.hourly_rate,
                    role.role_id,
                    role.role_name
                FROM
                    profile
                LEFT JOIN
                    role
                ON 
                    role.role_id = profile.role_id
                JOIN 
                    assignment
                ON 
                    assignment.profile_id = profile.profile_id
                WHERE assignment.project_id = ?;
                """;

        return jdbcTemplate.query(sql, rowMapper, projectId);
    }


    public User findUserByEmail(String email) {
        String sql = """
                SELECT 
                    p.profile_id, 
                    p.username, 
                    p.password, 
                    p.email, 
                    p.hourly_rate, 
                    r.role_name,
                    r.role_id
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
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findUserById(int profileId) {

        String sql = """
                SELECT 
                    p.profile_id, 
                    p.username, 
                    p.email, 
                    p.password, 
                    p.hourly_rate,
                    r.role_id,
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

        return jdbcTemplate.queryForObject(sql, rowMapper, profileId);
    }

    //  SAVE/CREATE USER/PROFILE

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


        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getHourlyRate(),
                user.getRole().getId());

    }

    // EDIT USER/PROFILE

    public void editProfile(User user) {
        String sql = """
                UPDATE profile
                SET 
                    username = ?, 
                    password = ?, 
                    email = ?,
                    hourly_rate = ?,
                    role_id =?
                WHERE 
                    profile_id = ?
                """;

        jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getHourlyRate(),
                user.getRole().getId(),

                user.getId());
    }

    // DELETE USER/PROFILE

    public void deleteProfile(int profileId) {
        String sql = """
                DELETE FROM profile
                WHERE profile_id = ?
                """;
        jdbcTemplate.update(sql, profileId);
    }
}

