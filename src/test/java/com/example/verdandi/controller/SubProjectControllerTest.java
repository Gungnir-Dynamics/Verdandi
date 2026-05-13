package com.example.verdandi.controller;

import com.example.verdandi.model.SubProject;

import com.example.verdandi.service.ProjectService;
import com.example.verdandi.service.SubProjectService;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@WebMvcTest(SubProjectController.class)
class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubProjectService subProjectService;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void getMySubProjects() throws Exception {

        when(subProjectService.getSubProjects(1))
                .thenReturn(List.of(new SubProject()));

        mockMvc.perform(get("/projects/1/subprojects"))
                .andExpect(status().isOk())
                .andExpect(view().name("sub_projects"))
                .andExpect(model().attributeExists("mySubProjects"))
                .andExpect(model().attributeExists("projectId"));
    }

    @Test
    void createNewSubProject() throws Exception {

        mockMvc.perform(get("/projects/1/subprojects/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_sub_project"))
                .andExpect(model().attributeExists("subProject"))
                .andExpect(model().attribute("projectId", 1));
    }

    @Test
    void editSubProject() throws Exception {

        SubProject subProject = new SubProject();
        subProject.setId(5);
        subProject.setProjectId(1);
        subProject.setName("Test");

        when(subProjectService.findSubProjectById(5, 1))
                .thenReturn(subProject);

        mockMvc.perform(get("/projects/1/subprojects/5/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_sub_project"))
                .andExpect(model().attributeExists("subProject"))
                .andExpect(model().attribute("projectId", 1));
    }

    @Test
    void updateSubProject() throws Exception {

        mockMvc.perform(
                        post("/projects/1/subprojects/5/edit")
                                .param("id", "5")
                                .param("projectId", "1")
                                .param("name", "Updated")
                                .param("description", "Updated desc")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/subprojects"));
    }
}