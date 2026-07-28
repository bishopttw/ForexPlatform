package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        if (userService.emailExists(email)) {
            model.addAttribute("error", "That email is already registered.");
            return "register";
        }

        if(!userService.isPasswordValid(password)){
            model.addAttribute("error", "Password must be at least 8 characters, with one capital letter and one symbol.");
            return "register";
        }

        userService.registerUser(email, username, password);
        return "redirect:/login?registered";
    }
}
