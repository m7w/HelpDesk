package com.training.helpdesk.ticket.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.config.TestConfig;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
public class TicketServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long MANAGER_ID = 2L;
    private static final Long ENGINEER_ID = 3L;
    private static final Long CATEGORY_ID = 1L;
    private static final Long TICKET_ID = 1L;
    private static final User EMPLOYEE = new User(EMPLOYEE_ID, "Lenny", "Leonart", Role.ROLE_EMPLOYEE, 
            "user1_mogilev@yopmail.com", "password");
    private static final User MANAGER = new User(MANAGER_ID, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");
    private static final User ENGINEER = new User(ENGINEER_ID, "Homer", "Simpson", Role.ROLE_ENGINEER, 
            "engineer1_mogilev@yopmail.com", "password");
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET = new Ticket(TICKET_ID, "Ticket1", "Ticket1 description",
            LocalDate.of(2022, 7, 10), LocalDate.of(2022, 8, 10), ENGINEER, EMPLOYEE,
            State.NEW, CATEGORY, Urgency.AVERAGE, MANAGER);
    private static final TicketDto DTO = new TicketDto(TICKET_ID, "Ticket1", LocalDate.of(2022, 7, 10),
            LocalDate.of(2022, 8, 10), Urgency.AVERAGE.ordinal(), "Average", State.NEW.ordinal(), "New", 
            CATEGORY_ID, "Application & Services", EMPLOYEE_ID, "Lenny Leonart", MANAGER_ID, "Seymour Skinner",
            ENGINEER_ID, "Homer Simpson", "Ticket1 description");

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketConverter ticketConverter;

    @Test
    public void testFindById() {
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(TICKET));
        when(ticketConverter.toDto(TICKET)).thenReturn(DTO);

        TicketDto actual = ticketService.findById(TICKET_ID);

        assertEquals(DTO, actual);
        verify(ticketRepository).findById(TICKET_ID);
        verify(ticketConverter).toDto(TICKET);
        verifyNoMoreInteractions(ticketRepository, ticketConverter);
    }

    @Test
    public void testFindById_NotFound() {
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.empty());
        String expected = "Ticket with id=1 not found";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> ticketService.findById(TICKET_ID));
        
        assertEquals(expected, exception.getMessage());
        verify(ticketRepository).findById(TICKET_ID);
        verifyNoMoreInteractions(ticketRepository);
        verifyNoInteractions(ticketConverter);
    }

    @Test
    @WithUserDetails(value = "user1_mogilev@yopmail.com", userDetailsServiceBeanName = "testUserDetailsService")
    public void testSecurityUserHasAccess_True() {
        when(ticketRepository.securityUserHasAccess(TICKET_ID, EMPLOYEE.getId(), Role.ROLE_EMPLOYEE, new QueryMetadata()))
            .thenReturn(true);

        assertTrue(ticketService.securityUserHasAccess(TICKET_ID));

        verify(ticketRepository).securityUserHasAccess(TICKET_ID, EMPLOYEE.getId(), Role.ROLE_EMPLOYEE, new QueryMetadata());
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    @WithUserDetails(value = "user1_mogilev@yopmail.com", userDetailsServiceBeanName = "testUserDetailsService")
    public void testSecurityUserHasAccess_False() {
        when(ticketRepository.securityUserHasAccess(TICKET_ID, EMPLOYEE.getId(), Role.ROLE_EMPLOYEE, new QueryMetadata()))
            .thenReturn(false);

        assertFalse(ticketService.securityUserHasAccess(TICKET_ID));

        verify(ticketRepository).securityUserHasAccess(TICKET_ID, EMPLOYEE.getId(), Role.ROLE_EMPLOYEE, new QueryMetadata());
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    @WithUserDetails(value = "user1_mogilev@yopmail.com", userDetailsServiceBeanName = "testUserDetailsService")
    public void testSecurityUserIsOwner_True() {
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(TICKET));
        when(ticketConverter.toDto(TICKET)).thenReturn(DTO);

        assertTrue(ticketService.securityUserIsOwner(TICKET_ID));

        verify(ticketRepository).findById(TICKET_ID);
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    @WithUserDetails(value = "manager1_mogilev@yopmail.com", userDetailsServiceBeanName = "testUserDetailsService")
    public void testSecurityUserIsOwner_False() {
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(TICKET));
        when(ticketConverter.toDto(TICKET)).thenReturn(DTO);

        assertFalse(ticketService.securityUserIsOwner(TICKET_ID));

        verify(ticketRepository).findById(TICKET_ID);
        verifyNoMoreInteractions(ticketRepository);
    }
}
