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

        if (rs.getDate("createdDate") != null) {
            project.setCreationDate(rs.getDate("createdDate").toLocalDate());
        }
        if (rs.getDate("deadline") != null) {
            project.setDeadline(rs.getDate("deadline").toLocalDate());
        }
        return project;

    };

    public List<Project> getProjects (){
        String sql = """
                SELECT *
                From Project
               
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }


}
