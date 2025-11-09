package com.baseproject.springapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewPageController {

    @GetMapping("/")
    public String home() {
       
         System.out.println("✅ Home page called");
        return "index";
    }

     @GetMapping("/manageExpenses")
    public String exp_dash() {
        System.out.println("✅ Expense dashboard page called");
        return "manage_expense_dash";
    }

     @GetMapping("/register")
    public String showRegistrationPage() {
        // This returns the name of the Thymeleaf template
        // (Spring automatically looks in /templates/)
        return "registrationForm";
    }

}
