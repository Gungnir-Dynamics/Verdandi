package com.example.verdandi.controller;

import com.example.verdandi.exception.ValidationException;
import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @MockitoBean
    private ProjectService projectService;


    @Autowired
    private MockMvc mockMvc;


//    @Test
//    void MyProjects_ShouldReturnProjects() throws Exception {
//
//        Project project1 = new Project();
//        project1.setId(1);
//        project1.setName("Website Redesign");
//        project1.setDescription("Test project");
//
//        Project project2 = new Project();
//        project2.setId(2);
//        project2.setName("Mobil App development");
//
//        when(projectService.getMultipleProjects()).thenReturn(List.of(project1, project2));
//
//
//        mockMvc.perform(get("/projects"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("/project/projects"))
//                .andExpect(model().attributeExists("myProjects"));
//    }

    @Test
    void showCreateNewProject_ShouldShowCreationForm() throws Exception {

        mockMvc.perform(get("/projects/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/project/create_project"))
                .andExpect(model().attributeExists("project"));

    }

    @Test
    void saveProject_ValidData_ShouldRedirectWithSuccess() throws Exception {

        mockMvc.perform(post("/projects/create")
                        .param("name", "Nyt Test Projekt")
                        .param("description", "Dette er en test")
                        .param("deadline", "2026-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(projectService).saveProject(any(Project.class));

    }
    // ==================== GET - CREATE FORM ====================
    @Test
    void showCreateForm_ShouldDisplayEmptyForm() throws Exception {

        mockMvc.perform(get("/projects/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/project/create_project"))
                .andExpect(model().attributeExists("project"));
    }

    // ==================== POST - CREATE SUCCESS ====================
    @Test
    void saveProject_ValidData_ShouldRedirectWithSuccesss() throws Exception {

        mockMvc.perform(post("/projects/create")
                        .param("name", "Nyt Internt System")
                        .param("description", "Udvikling af nyt intern værktøj")
                        .param("deadline", "2026-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(projectService).saveProject(any(Project.class));
    }

    // ==================== POST - CREATE WITH VALIDATION ERROR ====================
    @Test
    void saveProject_ServiceException_ShouldShowErrorMessage() throws Exception {

        doThrow(new ValidationException("Deadline can not be before today's date"))
                .when(projectService)
                .saveProject(any(Project.class));

        mockMvc.perform(post("/projects/create")
                        .param("name", "Test Projekt"))

                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Deadline can not be before today's date"));


    }


    // ==================== POST - EDIT SUCCESS ====================
    @Test
    void updateProject_ValidData_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/projects/1/edit")
                        .param("name", "Website Redesign - Opdateret")
                        .param("description", "Ny og forbedret beskrivelse")
                        .param("deadline", "2026-09-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(flash().attributeExists("successMessage"));
    }




    // ==================== DELETE ====================
    @Test
    void deleteProject_ShouldRedirectWithSuccess() throws Exception {

        mockMvc.perform(post("/projects/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(flash().attributeExists("successMessage"));
    }
}
