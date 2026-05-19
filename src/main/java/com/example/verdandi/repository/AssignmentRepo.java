package com.example.verdandi.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepo {

    private final JdbcTemplate jdbcTemplate;

    public AssignmentRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userHasAccessToProject(int profileId, int projectId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM assignment
                    WHERE profile_id = ? AND project_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, profileId, projectId);
        return count != null && count > 0;
    }

    public void addUserToProject(int profileId, int projectId) {
        String sql = "INSERT INTO assignment (profile_id, project_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, profileId, projectId);
    }

    public void removeUserFromProject(int profileId, int projectId) {
        String sql = "DELETE FROM assignment WHERE profile_id = ? AND project_id = ?";
        jdbcTemplate.update(sql, profileId, projectId);
    }

    public boolean isUserAlreadyAssigned(int profileId, int projectId) {
        String sql = "SELECT COUNT(*) FROM assignment WHERE profile_id = ? AND project_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, profileId, projectId);
        return count != null && count > 0;
    }
}
