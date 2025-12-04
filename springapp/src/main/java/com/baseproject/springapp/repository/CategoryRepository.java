package com.baseproject.springapp.repository;

import com.baseproject.springapp.model.ExpCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<ExpCategory, Long> {
    // fetch only active categories (delete_status = 0)
    List<ExpCategory> findByDeleteStatus(Integer deleteStatus);
}
