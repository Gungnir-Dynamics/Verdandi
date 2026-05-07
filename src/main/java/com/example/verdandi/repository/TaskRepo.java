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
        task.setEstimatedTime(rs.getInt("estimated_time"));

        return task;
    };

    public List<Task> getTasks(int subprojectId) {
        String sql = """
                SELECT task_id, name, description, estimated_time
                FROM task
                WHERE sub_project_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, subprojectId);
    }

}
