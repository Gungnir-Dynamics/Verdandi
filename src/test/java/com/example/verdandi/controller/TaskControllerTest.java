package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Task;
import com.example.verdandi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(model().attribute("tasks", List.of(task)))
                .andExpect(model().attribute("projectId", projectId));
    }

    @Test
    void showCreateForm_returnsViewAndModel() throws Exception {
        mockMvc.perform(get("/projects/1/subprojects/2/tasks/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/create_task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("projectId", 1))
                .andExpect(model().attribute("subprojectId", 2));
    }

    @Test
    void createTask_validData_redirectsToTaskList() throws Exception {
        mockMvc.perform(post("/projects/1/subprojects/2/tasks/create")
                        .param("name", "Test Task")
                        .param("description", "Some description")
                        .param("estimatedHours", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/subprojects/2/tasks"));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(taskService).createTask(eq(1), eq(2), captor.capture());

        assertEquals("Test Task", captor.getValue().getName());
        assertEquals("Some description", captor.getValue().getDescription());
        assertEquals(5, captor.getValue().getEstimatedHours());
    }

    @Test
    void createTask_invalidData_showCreationForm() throws Exception {
        doThrow(new ValidationException("Task name cannot be empty."))
                .when(taskService)
                .createTask(eq(1), eq(2), any(Task.class));

        mockMvc.perform(post("/projects/1/subprojects/2/tasks/create")
                        .param("name", "")
                        .param("description", "desc")
                        .param("estimatedHours", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/create_task"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void showEditForm_returnsViewAndModel() throws Exception {
        Task task = new Task(3, "Fix bug", "Desc", 5);
        when(taskService.getSingleTask(1, 2, 3)).thenReturn(task);

        mockMvc.perform(get("/projects/1/subprojects/2/tasks/3/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit_task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("projectId", 1))
                .andExpect(model().attribute("subprojectId", 2))
                .andExpect(model().attribute("taskId", 3));
    }

    @Test
    void updateTask_validData_redirectsToTaskList() throws Exception {
        mockMvc.perform(post("/projects/1/subprojects/2/tasks/3/edit")
                        .param("name", "Updated Task")
                        .param("description", "Updated description")
                        .param("estimatedHours", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/subprojects/2/tasks"));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(taskService).updateTask(eq(1), eq(2), eq(3), captor.capture());

        assertEquals("Updated Task", captor.getValue().getName());
        assertEquals("Updated description", captor.getValue().getDescription());
        assertEquals(10, captor.getValue().getEstimatedHours());
    }

    @Test
    void updateTask_invalidData_showCreationForm() throws Exception {
        doThrow(new ValidationException("Task name cannot be empty."))
                .when(taskService)
                .updateTask(eq(1), eq(2), eq(3), any(Task.class));

        mockMvc.perform(post("/projects/1/subprojects/2/tasks/3/edit")
                        .param("name", "")
                        .param("description", "desc")
                        .param("estimatedHours", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit_task"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void deleteTask_valid_redirectsToTaskList() throws Exception {
        mockMvc.perform(post("/projects/1/subprojects/2/tasks/3/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/subprojects/2/tasks"));

        verify(taskService).deleteTask(1, 2, 3);
    }
}