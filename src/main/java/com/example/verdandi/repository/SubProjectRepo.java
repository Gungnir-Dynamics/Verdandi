package com.example.verdandi.repository;

import com.example.verdandi.model.Project;
import com.example.verdandi.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepo {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<SubProject> rowMapper = (rs, rowNum) -> {
        SubProject subProject = new SubProject();
        subProject.setId(rs.getInt("sub_project_id"));
        subProject.setName(rs.getString("name"));
        subProject.setDescription(rs.getString("description"));
        subProject.setProjectId(rs.getInt("project_id"));

        return subProject;

    };

    public List<SubProject> getSubProjects(int projectId) {
        String sql = """
                SELECT *
                From sub_project
                where project_id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, projectId);
    }

    public boolean subprojectBelongsToProject(int projectId, int subprojectId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM sub_project
                    WHERE sub_project_id = ? AND project_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, subprojectId, projectId);
        return count != null && count > 0;
    }

    public void createSubProject(SubProject subProject) {
        String sql = """
                
                INSERT INTO sub_project (name, description, project_id)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getProjectId()
        );
    }

    public void updateSubProject(SubProject subProject) {
        String sql = """
                UPDATE sub_project 
                SET name = ?, description = ? 
                WHERE sub_project_id = ?""";
        jdbcTemplate.update(
                sql,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getId()
        );

    }
    public void deleteSubProject(int id) {
        String sql = """
                DELETE FROM sub_project 
                WHERE sub_project_id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
    public SubProject findSubProjectById (int id) {
        String sql = """
                SELECT * 
                FROM sub_project 
                WHERE sub_project_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}