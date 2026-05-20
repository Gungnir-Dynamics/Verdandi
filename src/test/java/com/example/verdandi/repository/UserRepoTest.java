package com.example.verdandi.repository;

import com.example.verdandi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_CLASS)
class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Test
    void getUsers() {

        List<User> users = userRepo.getUsers();
        assertThat(users).isNotNull();

    }

    @Test
    void findUserByEmail() {

        User user = userRepo.findUserByEmail("anders@example.com");

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("anders@example.com");
        assertThat(user.getPassword()).isEqualTo("anders123");
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getHourlyRate()).isEqualTo(850);
        assertThat(user.getRole()).isEqualTo("admin");

    }

    @Test
    void findUserById() {

        User user = userRepo.findUserById(1);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("anders@example.com");
        assertThat(user.getPassword()).isEqualTo("anders123");
        assertThat(user.getHourlyRate()).isEqualTo(850);
        assertThat(user.getRole()).isEqualTo("admin");
        assertThat(user.getId()).isEqualTo(1);

    }

    @Test
    void saveUser() {

        User testUser = new User(
                9,
                "TEST-USER",
                "TEST-PASSWORD",
                "TEST@MAIL.TEST",
                1000,
                "user");

        userRepo.saveUser(testUser);

        User savedTestUser = userRepo.findUserByEmail("TEST@MAIL.TEST");

        assertThat(savedTestUser).isNotNull();
        assertThat(savedTestUser.getUsername()).isEqualTo("TEST-USER");
        assertThat(savedTestUser.getEmail()).isEqualTo("TEST@MAIL.TEST");
        assertThat(savedTestUser.getPassword()).isEqualTo("TEST-PASSWORD");
        assertThat(savedTestUser.getId()).isEqualTo(9);
        assertThat(savedTestUser.getHourlyRate()).isEqualTo(1000);
        assertThat(savedTestUser.getRole()).isEqualTo("user");

    }

    @Test
    void editProfile() {

        User testUser = userRepo.findUserByEmail("anders@example.com");

        assertThat(testUser.getId()).isEqualTo(1);
        assertThat(testUser.getUsername()).isEqualTo("Anders Jensen");
        assertThat(testUser.getEmail()).isEqualTo("anders@example.com");
        assertThat(testUser.getPassword()).isEqualTo("anders123");
        assertThat(testUser.getHourlyRate()).isEqualTo(850);
        assertThat(testUser.getRole()).isEqualTo("admin");

        testUser.setUsername("Anders TEST");
        testUser.setEmail("AndersTest@example.com");
        testUser.setPassword("AndersTest123");
        testUser.setRole("user");

        userRepo.saveUser(testUser);

        User updatedUser = userRepo.findUserByEmail("AndersTest@example.com");

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("Anders TEST");
        assertThat(updatedUser.getEmail()).isEqualTo("AndersTest@example.com");
        assertThat(updatedUser.getPassword()).isEqualTo("AndersTest123");
        assertThat(updatedUser.getRole()).isEqualTo("user");

    }

    @Test
    void deleteProfile() {
        User user = userRepo.findUserById(5);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(5);

        userRepo.deleteProfile(5);

        User deletedUser = userRepo.findUserById(5);
        assertThat(deletedUser).isNull();

    }
}