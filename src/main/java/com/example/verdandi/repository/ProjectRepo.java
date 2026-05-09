package com.example.verdandi.repository;


import com.example.verdandi.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public class ProjectRepo {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepo (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<Project> rowMapper = (rs, rowNum) ->{
      Project project = new Project();
      project.setId(rs.getInt("project_id"));
      project.setName(rs.getString("name"));
      project.setDescription(rs.getString("description"));

        if (rs.getDate("created_date") != null) {
            project.setCreationDate(rs.getDate("created_date").toLocalDate());
        }
        if (rs.getDate("deadline") != null) {
            project.setDeadline(rs.getDate("deadline").toLocalDate());
        }
        return project;

    };



    public List<Project> getMultipleProjects(){
        String sql = """
                SELECT *
                From Project
               
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Project getSingleProject(int projectId){
        String sql = """
                SELECT *
                FROM Project
                WHERE Project.project_id = ?;
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, projectId);
    }

    public void createProject(Project project){
        String sql = "INSERT INTO Project (name, description, deadline) values (?, ?, ?)";
        jdbcTemplate.update(sql, project.getName(), project.getDescription(), project.getDeadline());
    }

    public void deleteProject(int projectId){
        String sql = "DELETE FROM project WHERE project_id = ?";
        jdbcTemplate.update(sql, projectId);
    }


    public boolean projectExists(int projectId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM project
                    WHERE project_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        return count != null && count > 0;
    }

}
