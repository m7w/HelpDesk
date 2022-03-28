package com.training.helpdesk.category.repository;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.category.domain.Category;

public interface CategoryRepository {

    List<Category> findAll();

    Optional<Category> findById(Long id);
}
