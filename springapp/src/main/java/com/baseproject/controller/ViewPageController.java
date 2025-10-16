package com.baseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewPageController {

    @GetMapping("/")
    public String home() {
        // model.addAttribute("users", repo.findAll());
        // model.addAttribute("user", new User());
        // model.addAttribute("customer", new Customer());
         System.out.println("✅ Home page called");
        return "index";
    }

}
