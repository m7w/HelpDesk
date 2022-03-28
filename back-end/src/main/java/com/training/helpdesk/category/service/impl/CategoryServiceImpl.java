package com.training.helpdesk.category.service.impl;

import java.util.List;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.repository.CategoryRepository;
import com.training.helpdesk.category.service.CategoryService;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

	@Override
	public Category findById(Long id) {
		return categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category with id=" + id + " not found"));
	}
}
