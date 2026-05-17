package com.example.verdandi.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepo {

    private final JdbcTemplate jdbcTemplate;

    public AssignmentRepo (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserAssignedToProject(int projectId, int profileId) {
        String sql = """
                SELECT COUNT(*)
                FROM assignment
                WHERE project_id = ? AND profile_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId, profileId);
        return count != null && count > 0;
    }
}
