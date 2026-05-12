package com.example.verdandi.repository;

import com.example.verdandi.model.SubProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<SubProject> result = subProjectRepo.getSubProjects(1);

        assertTrue(
                result.stream()
                        .anyMatch(sp -> sp.getName().equals("H2 database test"))
        );
    }

    @Test
    void updateSubProject() {

        // Noter Timothy: subproject oprettes i H2 databasen
        SubProject subProject = new SubProject();
        subProject.setName("Test");
        subProject.setDescription("test");
        subProject.setProjectId(1);
        subProject.setEstimatedHours(10);

        subProjectRepo.createSubProject(subProject, 1);

        // Noter Timothy: Kalder alle "subprojects", og leder/finder den som vi lige har oprettet
        SubProject updatedSubproject = subProjectRepo.getSubProjects(1).stream()
                .filter(sp -> sp.getName().equals("Test"))
                .findFirst()
                .orElseThrow();

        // Noter Timothy: Opdatere data
        updatedSubproject.setName("Updated test name");
        updatedSubproject.setDescription("Updated test description");


        subProjectRepo.updateSubProject(updatedSubproject.getId(), updatedSubproject);

        //Noter Timothy: Henter (opdateret)data fra databasen
        SubProject updated = subProjectRepo.findSubProjectById(updatedSubproject.getId());



        //Noter Timothy: Her verificere jeg om dataen er blevet opdateret
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


        subProjectRepo.createSubProject(subProject, 1);

        SubProject created = subProjectRepo.getSubProjects(1).stream()
                .filter(sp -> sp.getName().equals("DELETE TEST"))
                .findFirst()
                .orElseThrow();

        int id = created.getId();

        subProjectRepo.deleteSubProject(id);

        assertThrows(Exception.class, () -> {
            subProjectRepo.findSubProjectById(id);
        });

    }

    @Test
    void findSubProjectById() {

        SubProject subProject = new SubProject();
        subProject.setName("Find subproject");
        subProject.setDescription("Find subproject test");
        subProject.setEstimatedHours(10);
        subProject.setProjectId(1);

        subProjectRepo.createSubProject(subProject, 1);

        SubProject created = subProjectRepo.getSubProjects(1).stream()
                .filter(sp -> sp.getName().equals("Find subproject"))
                .findFirst()
                .orElseThrow();

        int id = created.getId();

        SubProject result = subProjectRepo.findSubProjectById(id);

        assertNotNull(result);
        assertEquals("Find subproject", result.getName());
        assertEquals("Find subproject test", result.getDescription());
    }
}