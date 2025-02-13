package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormController {

    @Value("${submit.endpoint}")
    private String submitEndpoint;

    @GetMapping("/post")
    public String sayHello(Model model) {
        model.addAttribute("message", "Hello, User!");
        model.addAttribute("submitEndpoint", submitEndpoint);
        return "form";  // Corresponds to hello.jsp
    }

    @PostMapping("/submit")
    public String handlePostRequest(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            Model model) {

        // Add form data to the model
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "result"; // Redirect to result.jsp
    }

}
