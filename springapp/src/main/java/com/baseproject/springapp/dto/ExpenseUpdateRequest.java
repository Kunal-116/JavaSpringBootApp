package com.baseproject.springapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseUpdateRequest {

    private Long expenseId;
    private LocalDate expenseDate;
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    
    // Getters and setters
    public Long getExpenseId() {
        return expenseId;
    }   

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    
}
