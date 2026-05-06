package com.example.verdandi.repository;

import com.example.verdandi.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
class ProjectRepoTest {

    @Autowired
    private ProjectRepo projectRepo;

    @Test
    void getProjects() {
        List<Project> all = projectRepo.getProjects();

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(4);
        assertThat(all.get(0).getName()).isEqualTo("Website Redesign");




    }
}