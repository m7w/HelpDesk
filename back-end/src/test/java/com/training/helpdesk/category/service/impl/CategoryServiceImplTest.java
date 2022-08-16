package com.training.helpdesk.category.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    private static final Long CATEGORY1_ID = 1L;
    private static final Category CATEGORY1 = new Category(CATEGORY1_ID, "Category 1");
    private static final Long CATEGORY2_ID = 2L;
    private static final Category CATEGORY2 = new Category(CATEGORY2_ID, "Category 2");

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void testFindAll() {
        final int expectedSize = 2;
        when(categoryRepository.findAll()).thenReturn(List.of(CATEGORY1, CATEGORY2));

        List<Category> actual = categoryService.findAll();

        assertEquals(expectedSize, actual.size());
        assertEquals(CATEGORY1, actual.get(0));
        assertEquals(CATEGORY2, actual.get(1));
        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void testFindById_NotFound() {
        when(categoryRepository.findById(CATEGORY1_ID)).thenReturn(Optional.empty());
        String expected = "Category with id=1 not found";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> categoryService.findById(CATEGORY1_ID));
        
        assertEquals(expected, exception.getMessage());
        verify(categoryRepository).findById(CATEGORY1_ID);
        verifyNoMoreInteractions(categoryRepository);
    }
}
