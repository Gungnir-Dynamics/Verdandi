package com.example.verdandi.repository;

import com.example.verdandi.model.Project;
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
    void taskBelongsToSubproject_returnsTrue() {
        assertTrue(taskRepo.taskBelongsToSubproject(1, 3));
    }

    @Test
    void taskBelongsToSubproject_returnsFalse() {
        assertFalse(taskRepo.taskBelongsToSubproject(1, 4));
    }
}
