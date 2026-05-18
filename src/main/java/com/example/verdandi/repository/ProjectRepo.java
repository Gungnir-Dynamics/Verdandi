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
      project.setEstimatedHours(rs.getInt("estimated_hours"));

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
                SELECT project.project_id, project.name, project.description, project.created_date, project.deadline, COALESCE(sum(task.estimated_hours), 0) AS estimated_hours
                From Project
                LEFT JOIN sub_project
                ON sub_project.project_id = project.project_id
                LEFT JOIN task
                ON task.sub_project_id = sub_project.sub_project_id
                GROUP BY project.project_id, project.name;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Project getSingleProject(int projectId){
        String sql = """
               SELECT project.project_id, project.name, project.description, project.created_date, project.deadline, COALESCE(sum(task.estimated_hours), 0) AS estimated_hours
               From Project
               LEFT JOIN sub_project
               ON sub_project.project_id = project.project_id
               LEFT JOIN task
               ON task.sub_project_id = sub_project.sub_project_id
               WHERE project.project_id = ?
               GROUP BY project.project_id, project.name, project.description, project.created_date, project.deadline;
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, projectId);
    }

    public List<Project> getAssignedProjects(int profileId) {
        String sql = """
                SELECT 
                    p.project_id,
                    p.name,
                    p.description,
                    p.created_date,
                    p.deadline,
                    COALESCE(SUM(t.estimated_hours), 0) AS estimated_hours
                       FROM project p
                       JOIN assignment a
                           ON a.project_id = p.project_id
                       LEFT JOIN sub_project sp
                           ON sp.project_id = p.project_id
                       LEFT JOIN task t
                           ON t.sub_project_id = sp.sub_project_id
                       WHERE a.profile_id = ?
                       GROUP BY p.project_id, p.name, p.description, p.created_date, p.deadline;
                """;
        return jdbcTemplate.query(sql, rowMapper, profileId);

    }


    public void createProject(Project project){
        String sql = "INSERT INTO Project (name, description, deadline) values (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                project.getName(),
                project.getDescription(),
                project.getDeadline());
    }

    public void deleteProject(int projectId){
        String sql = "DELETE FROM project WHERE project_id = ?";
        jdbcTemplate.update(sql, projectId);
    }

    public void updateProject(int projectId, Project project){
        String sql = """
                UPDATE Project
                SET name = ?, description = ?, deadline = ?
                WHERE project_id = ?
                """;

        jdbcTemplate.update(
                sql,
                project.getName(),
                project.getDescription(),
                project.getDeadline(),
                projectId
        );
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
