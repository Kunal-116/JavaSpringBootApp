package com.baseproject.springapp.repository;

import com.baseproject.springapp.model.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExpenseRepository extends JpaRepository<UserExpense, Long> 
{
    
}
