package com.training.helpdesk.category.service;

import java.util.List;

import com.training.helpdesk.category.domain.Category;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);
}
