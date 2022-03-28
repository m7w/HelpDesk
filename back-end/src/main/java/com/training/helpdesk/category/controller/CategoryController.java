package com.training.helpdesk.category.controller;

import java.util.List;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.service.CategoryService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> findAll() {

        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }
}
