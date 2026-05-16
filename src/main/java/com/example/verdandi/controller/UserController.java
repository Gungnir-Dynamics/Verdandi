package com.example.verdandi.controller;

import com.example.verdandi.model.User;
import com.example.verdandi.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (userService.login(email, password)) {
            session.setAttribute("user", userService.findUserByEmail(email));
            return "redirect:/project/projects";
        }
        // WRONG INPUT
        model.addAttribute("wrongCredentials", true);
        return "auth/login";
    }

    @GetMapping("/editProfile")
    public String showEditProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "auth/editProfile";
    }

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute User profile, HttpSession session) {
        userService.editProfile(profile);

        User updatedUser = userService.findUserByEmail(profile.getEmail());

        session.setAttribute("user", updatedUser);
        return "redirect:/projects";

    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                 @RequestParam String newPassword, Model model) {

        User user = userService.findUserByEmail(email);

        if (user == null) {
            model.addAttribute("EmailNotFound", true);
            return "auth/forgot-password";
        }
        user.setPassword(newPassword);
        userService.editProfile(user);

        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}