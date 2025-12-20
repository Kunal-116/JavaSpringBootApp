package com.baseproject.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Use @Controller
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.baseproject.springapp.util.SecurityUtil;

@Controller
public class ViewPageController {

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "registrationForm";
    }

    // Protected dashboard
    @GetMapping("/manageExpenses")
    public String manageExpenses(Model model) {

        Long userId = securityUtil.getLoggedInUserId();

        model.addAttribute("currentUserId", userId);

        return "manage_expense_dash";
    }

}