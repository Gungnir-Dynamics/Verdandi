package com.example.verdandi.repository;

import com.example.verdandi.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
class ProjectRepoTest {

    @Autowired
    private ProjectRepo projectRepo;

    @Test
    void getProjects() {
        List<Project> all = projectRepo.getMultipleProjects();

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(4);
        assertThat(all.get(0).getName()).isEqualTo("Website Redesign");

    }

    @Test
    void getSingleProject() {
        Project project = projectRepo.getSingleProject(1);

        assertThat(project.getName()).isEqualTo("Website Redesign");
        assertThat(project.getDescription()).isEqualTo("Total redesign af virksomhedens hjemmeside med ny branding og moderne design");
        assertThat(project.getEstimatedHours()).isEqualTo(331);
    }


    @Test
    void createProject() {
        Project project = new Project();
        project.setName("Creation Test");
        project.setDescription("Testing description with H2");
        project.setDeadline(LocalDate.of(2026, 12, 31));

        projectRepo.createProject(project);

        Project mostRecentProject = projectRepo.getSingleProject(5);

        assertThat(mostRecentProject).isNotNull();
        assertThat(mostRecentProject.getName()).isEqualTo("Creation Test");
        assertThat(mostRecentProject.getDescription()).isEqualTo("Testing description with H2");
        assertThat(mostRecentProject.getDeadline()).isEqualTo(LocalDate.of(2026,12,31));

    }

    @Test
    void updateProject(){
        Project updated = new Project();
        updated.setName("Website Redesign - Opdateret");
        updated.setDescription("Ny beskrivelse");
        updated.setDeadline(LocalDate.of(2026, 9, 1));

        projectRepo.updateProject(1, updated);

        Project database = projectRepo.getSingleProject(1);
        assertThat(database.getName()).isEqualTo("Website Redesign - Opdateret");
        assertThat(database.getDescription()).isEqualTo("Ny beskrivelse");
        assertThat(database.getDeadline()).isEqualTo(LocalDate.of(2026, 9, 1));
    }

    @Test
    void deleteProject() {
        projectRepo.deleteProject(1);

        assertThat(projectRepo.projectExists(1)).isFalse();
    }

    @Test
    void projectExists() {
        assertThat(projectRepo.projectExists(2)).isTrue();
        assertThat(projectRepo.projectExists(999)).isFalse();
    }
}