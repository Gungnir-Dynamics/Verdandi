package com.example.verdandi.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserControllerTest.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;



    @Test
    void registerPage() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void register() {
    }

    @Test
    void loginPage() {
    }

    @Test
    void login() {
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