package com.baseproject.springapp.controller;

import com.baseproject.springapp.model.UserExpense;
import com.baseproject.springapp.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expmgmt")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping("/add_expense")
    public String addExpense(
            @RequestParam("userId") Long userId,
            @RequestParam("date") String date,
            @RequestParam("category") Long categoryId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam(value = "description", required = false) String description
    ) {

        UserExpense exp = new UserExpense();
        exp.setUserId(userId);
        exp.setCategoryId(categoryId);
        exp.setAmount(amount);
        exp.setNote(description);
        exp.setExpenseDate(LocalDate.parse(date));
        exp.setDeleteStatus("0"); // active

        expenseRepository.save(exp);

        return "redirect:/manageExpenses";
    }
}
