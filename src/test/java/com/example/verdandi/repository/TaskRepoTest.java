package com.example.verdandi.repository;

import com.example.verdandi.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class TaskRepoTest {

    @Autowired
    private TaskRepo taskRepo;

    @Test
    void getTasks_returnsCorrectRows() {
        List<Task> all = taskRepo.getTasks(1);

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(3);
        assertThat(all.getFirst().getName()).isEqualTo("Lav wireframes");
    }

    @Test
    void getSingleTask() {
        Task task = taskRepo.getSingleTask(1);

        assertThat(task.getName()).isEqualTo("Lav wireframes");
        assertThat(task.getDescription()).isEqualTo("Low-fidelity wireframes af alle hoved sider");
        assertThat(task.getEstimatedHours()).isEqualTo(35);
    }

    @Test
    void taskBelongsToSubproject_returnsTrue() {
        assertTrue(taskRepo.taskBelongsToSubproject(1, 3));
    }

    @Test
    void taskBelongsToSubproject_returnsFalse() {
        assertFalse(taskRepo.taskBelongsToSubproject(1, 4));
    }

    @Test
    void createTask_insertsTaskIntoDatabase() {
        Task task = new Task();
        task.setName("Test Task");
        task.setDescription("This is a test task");
        task.setEstimatedHours(5);

        taskRepo.createTask(1, task);

        Task mostRecentTask = taskRepo.getSingleTask(17);

        assertThat(mostRecentTask.getName()).isEqualTo("Test Task");
        assertThat(mostRecentTask.getDescription()).isEqualTo("This is a test task");
        assertThat(mostRecentTask.getEstimatedHours()).isEqualTo(5);
    }

    @Test
    void updateTask_updatesRowInDatabase() {
        Task original = taskRepo.getSingleTask(1);
        assertThat(original.getName()).isEqualTo("Lav wireframes");

        Task updated = new Task();
        updated.setName("Updated name");
        updated.setDescription("Updated description");
        updated.setEstimatedHours(99);

        taskRepo.updateTask(1, updated);

        Task afterUpdate = taskRepo.getSingleTask(1);

        assertThat(afterUpdate.getName()).isEqualTo("Updated name");
        assertThat(afterUpdate.getDescription()).isEqualTo("Updated description");
        assertThat(afterUpdate.getEstimatedHours()).isEqualTo(99);
    }

    @Test
    void deleteTask_removesRowFromDatabase() {
        Task beforeDelete = taskRepo.getSingleTask(1);
        assertThat(beforeDelete).isNotNull();
        assertThat(beforeDelete.getName()).isEqualTo("Lav wireframes");

        taskRepo.deleteTask(1);

        assertFalse(taskRepo.taskBelongsToSubproject(1, 1));

        List<Task> remaining = taskRepo.getTasks(1);
        assertThat(remaining.size()).isEqualTo(2);
    }
}
