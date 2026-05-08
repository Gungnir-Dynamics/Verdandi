package com.example.verdandi.controller;

import com.example.verdandi.model.SubProject;
import com.example.verdandi.service.SubProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubProjectController.class)
class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubProjectService subProjectService;

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
}