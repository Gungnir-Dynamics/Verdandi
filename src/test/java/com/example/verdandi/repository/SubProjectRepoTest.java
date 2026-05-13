package com.example.verdandi.repository;

import com.example.verdandi.model.SubProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
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
        subProject.setEstimatedHours(10);

        subProjectRepo.createSubProject(subProject, 1);

        SubProject createdSubproject = subProjectRepo.findSubProjectById(11);

        assertThat(createdSubproject.getName()).isEqualTo("H2 database test");
        assertThat(createdSubproject.getDescription()).isEqualTo("H2 database test description");
        assertThat(createdSubproject.getProjectId()).isEqualTo(1);
    }

    @Test
    void updateSubProject() {

        SubProject subProject = new SubProject();
        subProject.setName("Test");
        subProject.setDescription("test");
        subProject.setProjectId(1);
        subProject.setEstimatedHours(10);

        subProjectRepo.createSubProject(subProject, 1);

        SubProject updatedSubproject = subProjectRepo.findSubProjectById(1);
        updatedSubproject.setName("Updated test name");
        updatedSubproject.setDescription("Updated test description");


        subProjectRepo.updateSubProject(updatedSubproject.getId(), updatedSubproject);

        SubProject updated = subProjectRepo.findSubProjectById(updatedSubproject.getId());



        assertEquals("Updated test name", updated.getName());
        assertEquals("Updated test description", updated.getDescription());

    }

    @Test
    void deleteSubProject() {

        SubProject subProject = new SubProject();
        subProject.setName("DELETE TEST");
        subProject.setDescription("DELETED");
        subProject.setEstimatedHours(10);
        subProject.setProjectId(1);

        assertThat(subProject).isNotNull();
        assertThat(subProject.getName()).isEqualTo("DELETE TEST");

        subProjectRepo.deleteSubProject(1);

        assertFalse(subProjectRepo.subprojectBelongsToProject(1, 1));

        List<SubProject> remainingSubprojects = subProjectRepo.getSubProjects(1);
        assertThat(remainingSubprojects.size()).isEqualTo(3);

    }

    @Test
    void findSubProjectById() {
        SubProject subProject = subProjectRepo.findSubProjectById(1);

        assertThat(subProject.getProjectId()).isEqualTo(1);
        assertThat(subProject.getName()).isEqualTo("Design fase");
        assertThat(subProject.getDescription()).isEqualTo("Wireframes, mockups og design af hele sitet");
    }
}