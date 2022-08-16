package com.training.helpdesk.ticket.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.EnumSet;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.category.service.CategoryService;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TicketConverterImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long MANAGER_ID = 2L;
    private static final Long ENGINEER_ID = 3L;
    private static final Long CATEGORY_ID = 1L;
    private static final Long TICKET1_ID = 1L;
    private static final Long TICKET2_ID = 2L;
    private static final User EMPLOYEE = new User(EMPLOYEE_ID, "Lenny", "Leonart", Role.ROLE_EMPLOYEE,
            "user1_mogilev@yopmail.com", "password");
    private static final User MANAGER = new User(MANAGER_ID, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");
    private static final User ENGINEER = new User(ENGINEER_ID, "Homer", "Simpson", Role.ROLE_ENGINEER,
            "engineer1_mogilev@yopmail.com", "password");
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET1 = new Ticket(TICKET1_ID, "Ticket1", "Ticket1 description", LocalDate.of(2022, 7, 10),
            LocalDate.of(2022, 8, 10), ENGINEER, EMPLOYEE, State.NEW, CATEGORY, Urgency.AVERAGE, MANAGER);
    private static final Ticket TICKET2 = new Ticket(TICKET2_ID, "Ticket2", "Ticket2 description", LocalDate.of(2022, 7, 11),
                LocalDate.of(2022, 8, 11), ENGINEER, EMPLOYEE, State.APPROVED, CATEGORY, Urgency.AVERAGE, MANAGER);

    @InjectMocks
    private TicketConverterImpl ticketConverter;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Test
    public void testToSmallDto() {
        TicketSmallDto dto1 = ticketConverter.toSmallDto(EMPLOYEE_ID, Role.ROLE_MANAGER, TICKET1);
        TicketSmallDto dto2 = ticketConverter.toSmallDto(EMPLOYEE_ID, Role.ROLE_ENGINEER, TICKET2);

        EnumSet<Action> expectedActions1 = EnumSet.of(Action.APPROVE, Action.DECLICE, Action.CANCEL);
        EnumSet<Action> expectedActions2 = EnumSet.of(Action.ASSIGN_TO_ME, Action.CANCEL);

        assertEquals(expectedActions1, dto1.getActions());
        assertEquals(expectedActions2, dto2.getActions());
    }

    @Test
    public void testToDto_ToEntity() {
        when(userService.findById(EMPLOYEE_ID)).thenReturn(EMPLOYEE);
        when(userService.findById(MANAGER_ID)).thenReturn(MANAGER);
        when(userService.findById(ENGINEER_ID)).thenReturn(ENGINEER);
        when(categoryService.findById(CATEGORY_ID)).thenReturn(CATEGORY);

        TicketDto dto = ticketConverter.toDto(TICKET1);

        Ticket actual = ticketConverter.toEntity(dto);

        assertEquals(TICKET1, actual);
        verify(userService, times(3)).findById(any());
        verify(categoryService).findById(CATEGORY_ID);
        verifyNoMoreInteractions(userService, categoryService);
    }
}
