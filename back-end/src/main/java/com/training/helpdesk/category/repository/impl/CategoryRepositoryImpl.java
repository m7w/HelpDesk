package com.training.helpdesk.category.repository.impl;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.repository.CategoryRepository;

import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private static final String FIND_ALL_CATEGORIES = "from Category";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Category> findAll() {
        return entityManager.createQuery(FIND_ALL_CATEGORIES, Category.class).getResultList();
    }

	@Override
	public Optional<Category> findById(Long id) {
		return Optional.ofNullable(entityManager.find(Category.class, id));
	}
}
