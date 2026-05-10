package com.example.verdandi.repository;

import com.example.verdandi.model.SubProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest
@ActiveProfiles
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
    class SubProjectRepoTest {

        @Autowired
        private SubProjectRepo subProjectRepo;

        @Test
        void createSubProject() {

            SubProject subProject = new SubProject();
            subProject.setName("H2 database test");
            subProject.setDescription("H2 database test description");
            subProject.setProjectId(1);

            subProjectRepo.createSubProject(subProject);

            List<SubProject> result = subProjectRepo.getSubProjects(1);

            assertTrue(
                    result.stream()
                            .anyMatch(sp -> sp.getName().equals("H2 database test"))
            );
        }
    }