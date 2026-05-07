package com.example.verdandi.controller;

import com.example.verdandi.model.Project;
import com.example.verdandi.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    void getMyProjects_FindProjects() throws Exception {

        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Website Redesign");
        project1.setDescription("Test projekt");

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Mobil App Udvikling");

        when(projectService.getMultipleProjects()).thenReturn(List.of(project1, project2));


        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attributeExists("myProjects"))
                .andExpect(model().attribute("myProjects", List.of(project1, project2)));
    }

    @Test
    void createNewProject() throws Exception {

        mockMvc.perform(get("/projects/create_project"))
                .andExpect(status().isOk())
                .andExpect(view().name("/create_project"))
                .andExpect(model().attributeExists("project"));

    }

    @Test
    void saveProject() throws Exception {

        mockMvc.perform(post("/projects/create_project")
                        .param("name", "Nyt Stort Projekt")
                        .param("description", "En rigtig god beskrivelse")
                        .param("deadline", "2026-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
               // .andExpect(flash().attributeExists("successMessage"));

        verify(projectService).saveProject(any(Project.class));
    }
}