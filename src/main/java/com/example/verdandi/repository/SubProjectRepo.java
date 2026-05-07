package com.example.verdandi.repository;

import com.example.verdandi.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepo {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepo (JdbcTemplate jdbcTemplate){
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

    public List<SubProject> getSubProjects (int projectId){
        String sql = """
               SELECT *
               From sub_project
               where project_id = ?
               """;

            return jdbcTemplate.query(sql, rowMapper, projectId);
    }
}