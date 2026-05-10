package com.example.verdandi.controller;

import com.example.verdandi.model.Project;
import com.example.verdandi.model.Task;
import com.example.verdandi.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @MockitoBean
    private ProjectService projectService;


    @Autowired
    private MockMvc mockMvc;


    @Test
    void MyProjects_ShouldReturnProjects() throws Exception {

        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Website Redesign");
        project1.setDescription("Test project");

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Mobil App development");

        when(projectService.getMultipleProjects()).thenReturn(List.of(project1, project2));


        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("/project/projects"))
                .andExpect(model().attributeExists("myProjects"));
    }

    @Test
    void showCreateNewProject_ShouldShowCreationForm() throws Exception {

        mockMvc.perform(get("/projects/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/project/create_project"))
                .andExpect(model().attributeExists("project"));

    }

//    @Test
//    void createProject_validData_redirectsToMyProjects() throws Exception {
//        mockMvc.perform(post("/projects/create")
//                        .param("name", "Test Projects")
//                        .param("description", "A description")
//                        .param("deadline", "2030,01,01"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/project/projects"));
//
//        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
//
//      //  verify(projectService).saveProject(eq(1), eq(2), captor.capture());
//
//        assertEquals("Test Task", captor.getValue().getName());
//        assertEquals("Some description", captor.getValue().getDescription());
//        assertEquals(5, captor.getValue().getEstimatedHours());
//    }


    // TEST VIRKER IKKE
    //java.lang.AssertionError: Range for response status value 404 expected:<REDIRECTION> but was:<CLIENT_ERROR>
    //Expected :REDIRECTION
    //Actual   :CLIENT_ERROR

    @Test
    void saveProject_WithInvalidDeadline_ReturnError() throws Exception{
        mockMvc.perform(post("/project/projects/create")
                .param("name", "Test Project")
                .param("deadline", "2020, 01, 01"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/create_project"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("errorMessage", "Deadline can not be before today's date"));
    }



    @Test
    void saveProject_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/projects/create_project")
                        .param("name", "A new big project")
                        .param("description", "A great description")
                        .param("deadline", "2026-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(projectService).saveProject(any(Project.class));
    }
}