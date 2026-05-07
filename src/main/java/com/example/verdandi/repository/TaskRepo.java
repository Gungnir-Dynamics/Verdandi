package com.example.verdandi.repository;

import com.example.verdandi.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepo {
    private final JdbcTemplate jdbcTemplate;

    public TaskRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getInt("task_id"));
        task.setName(rs.getString("name"));
        task.setDescription(rs.getString("description"));
        task.setEstimatedHours(rs.getInt("estimated_hours"));

        return task;
    };

    public List<Task> getTasks(int subprojectId) {
        String sql = """
                SELECT task_id, name, description, estimated_hours
                FROM task
                WHERE sub_project_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, subprojectId);
    }

    public boolean taskBelongsToSubproject(int subProjectId, int taskId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM task
                    WHERE task_id = ? AND sub_project_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, taskId, subProjectId);
        return count != null && count > 0;
    }

}
