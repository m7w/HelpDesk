package com.training.helpdesk.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;

@DisabledInAotMode
public class UserControllerTest extends AbstractControllerTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long MANAGER_ID = 2L;
    private static final Long ENGINEER_ID = 3L;
    private static final User EMPLOYEE =
            new User(
                    EMPLOYEE_ID,
                    "Lenny",
                    "Leonart",
                    Role.ROLE_EMPLOYEE,
                    "user1_mogilev@yopmail.com",
                    "password");
    private static final User MANAGER =
            new User(
                    MANAGER_ID,
                    "Seymour",
                    "Skinner",
                    Role.ROLE_MANAGER,
                    "manager1_mogilev@yopmail.com",
                    "password");
    private static final User ENGINEER =
            new User(
                    ENGINEER_ID,
                    "Homer",
                    "Simpson",
                    Role.ROLE_ENGINEER,
                    "engineer1_mogilev@yopmail.com",
                    "password");

    @MockBean private UserService userService;

    @Test
    public void testGetAll() throws Exception {
        final int expectedSize = 3;
        when(userService.findAll()).thenReturn(List.of(EMPLOYEE, MANAGER, ENGINEER));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(expectedSize)))
                .andExpect(jsonPath("$[0].id", is(EMPLOYEE_ID), Long.class))
                .andExpect(jsonPath("$[0].firstName", is("Lenny")))
                .andExpect(jsonPath("$[0].lastName", is("Leonart")))
                .andExpect(jsonPath("$[0].role", is("ROLE_EMPLOYEE")))
                .andExpect(jsonPath("$[0].email", is("user1_mogilev@yopmail.com")));

        verify(userService).findAll();
        verifyNoMoreInteractions(userService);
    }
}
