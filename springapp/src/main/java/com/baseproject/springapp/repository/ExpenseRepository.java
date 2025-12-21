package com.baseproject.springapp.repository;

import com.baseproject.springapp.model.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;              

@Repository
public interface ExpenseRepository extends JpaRepository<UserExpense, Long> 
{
     List<UserExpense> findByUserId(Long userId);
     Optional<UserExpense> findByExpenseIdAndUserId(Long expenseId, Long userId);
     List<UserExpense> findByUserIdAndDeleteStatus(Long userId, String deleteStatus);

     
     
}
