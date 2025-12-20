package com.baseproject.springapp.repository;

import com.baseproject.springapp.model.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<UserExpense, Long> 
{
     List<UserExpense> findByUserId(Long userId);
}
