package com.baseproject.springapp.controller;

import com.baseproject.springapp.model.ExpCategory;
import com.baseproject.springapp.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryRestController {

    private final CategoryRepository categoryRepository;

    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/categories")
    public List<ExpCategory> getActiveCategories() {
        // return categories with delete_status = 0
        return categoryRepository.findByDeleteStatus(0);
    }
}
