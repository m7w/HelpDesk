package com.training.helpdesk.category.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.service.CategoryService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class CategoryControllerTest extends AbstractControllerTest {

    private static final Long CATEGORY1_ID = 1L;
    private static final Category CATEGORY1 = new Category(CATEGORY1_ID, "Category 1");
    private static final Long CATEGORY2_ID = 2L;
    private static final Category CATEGORY2 = new Category(CATEGORY2_ID, "Category 2");

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAll() throws Exception {
        final int expectedSize = 2;
        when(categoryService.findAll()).thenReturn(List.of(CATEGORY1, CATEGORY2));

        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(expectedSize)))
            .andExpect(jsonPath("$[0].id", is(CATEGORY1_ID), Long.class))
            .andExpect(jsonPath("$[0].name", is("Category 1")))
            .andExpect(jsonPath("$[1].id", is(CATEGORY2_ID), Long.class))
            .andExpect(jsonPath("$[1].name", is("Category 2")));

        verify(categoryService).findAll();
        verifyNoMoreInteractions(categoryService);
    }

}
