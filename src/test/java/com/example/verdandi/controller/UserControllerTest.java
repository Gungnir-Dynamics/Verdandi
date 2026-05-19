package com.example.verdandi.controller;

import com.example.verdandi.model.User;
import com.example.verdandi.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.rmi.server.ExportException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    private HttpSession session;
    private User user;

    @BeforeEach
    void setup() {

        // Opret User
        user = new User();
        user.setId(10);
        user.setUsername("Controller test");
        user.setEmail("ControllerTest@mail.com");
        user.setPassword("1234");
        user.setHourlyRate(100);
        user.setRole("user");

        //Opret session

        session = new MockHttpSession();
        session.setAttribute("user", user);


    }


    @Test
    void registerPage() throws Exception {

        mockMvc.perform(get("/auth/register")
                .session((MockHttpSession)session))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void register() throws Exception {

        mockMvc.perform(post("/auth/register")
                        .param("email", "NewTest@mail.com")
                        .param("password", "PW1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(service).saveUser(captor.capture());

        User newUser = captor.getValue();
        assertEquals("NewTest@mail.com", newUser.getEmail());
        assertEquals("PW1234", newUser.getPassword());

    }

    @Test
    void loginPage() throws Exception {

        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));

    }

    @Test
    void login() throws Exception {

        when(service.login("test@example.com", "1234")).thenReturn(true);
        when(service.findUserByEmail("test@example.com")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .param("email", "test@example.com")
                        .param("password", "1234")
                        .session((MockHttpSession) session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

    }

    @Test
    void showEditProfile() {
    }

    @Test
    void editProfile() {
    }

    @Test
    void showForgotPassword() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void logout() {
    }
}