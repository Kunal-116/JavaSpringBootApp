package com.baseproject.springapp.controller;

import com.baseproject.springapp.model.UserExpense;
import com.baseproject.springapp.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.baseproject.springapp.util.SecurityUtil;
import com.baseproject.springapp.dto.ExpenseUpdateRequest;

@RestController
@RequestMapping("/api/expmgmt")
public class ExpenseController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping("/add_expense")
public String addExpense(
        @RequestParam("date") String date,
        @RequestParam("category") Long categoryId,
        @RequestParam("amount") BigDecimal amount,
        @RequestParam(value = "description", required = false) String description
) {

    Long userId = securityUtil.getLoggedInUserId(); 

    UserExpense exp = new UserExpense();
    exp.setUserId(userId);
    exp.setCategoryId(categoryId);
    exp.setAmount(amount);
    exp.setNote(description);
    exp.setExpenseDate(LocalDate.parse(date));
    exp.setDeleteStatus("0");

    expenseRepository.save(exp);

    return "OK"; // REST response
}


 @PostMapping("/loadUserExpenses")
public List<UserExpense> loadUserExpenses() {

    Long userId = securityUtil.getLoggedInUserId();

    return expenseRepository.findByUserIdAndDeleteStatus(userId, "0");
}



  @PutMapping("/updateExpense")
    public void update(@RequestBody ExpenseUpdateRequest req) {

        Long userId = securityUtil.getLoggedInUserId();

        UserExpense exp = expenseRepository
            .findByExpenseIdAndUserId(req.getExpenseId(), userId)
            .orElseThrow(() -> new RuntimeException("User Not found"));

        exp.setAmount(req.getAmount());
        exp.setCategoryId(req.getCategoryId());
        exp.setExpenseDate(req.getExpenseDate());
        exp.setNote(req.getDescription());


        expenseRepository.save(exp);
    }
    

    @DeleteMapping("/deleteExpense")
    public void deleteExpense(@RequestBody ExpenseUpdateRequest req) {

        Long userId = securityUtil.getLoggedInUserId();

        UserExpense exp = expenseRepository
            .findByExpenseIdAndUserId(req.getExpenseId(), userId)
            .orElseThrow(() -> new RuntimeException("Expense Not found"));

        exp.setDeleteStatus("1");

        expenseRepository.save(exp);
    }   
    
}
