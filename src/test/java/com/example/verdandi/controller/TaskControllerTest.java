package com.example.verdandi.controller;

import com.example.verdandi.exception.DatabaseOperationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @Test
    void showTasks_returnsTasks() throws Exception {
        int projectId = 1;
        int subprojectId = 2;

        Task task = new Task(1, "Fix bug", "Description", 5);
        when(taskService.getTasksBySubproject(projectId, subprojectId))
                .thenReturn(List.of(task));

        mockMvc.perform(get("/projects/{projectId}/subprojects/{subprojectId}/tasks",
                        projectId, subprojectId))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/task-list"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", List.of(task)));
    }
}