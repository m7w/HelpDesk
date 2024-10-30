package com.training.helpdesk.ticket.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.service.TicketService;
import com.training.helpdesk.ticket.util.AccessChecker;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.time.LocalDate;
import java.util.List;

@DisabledInAotMode
public class TicketControllerTest extends AbstractControllerTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long MANAGER_ID = 2L;
    private static final Long ENGINEER_ID = 3L;
    private static final Long CATEGORY_ID = 1L;
    private static final Long TICKET1_ID = 1L;
    private static final Long TICKET2_ID = 2L;
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
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET1 =
            new Ticket(
                    TICKET1_ID,
                    "Ticket1",
                    "Ticket1 description",
                    LocalDate.of(2022, 7, 10),
                    LocalDate.of(2022, 8, 10),
                    ENGINEER,
                    EMPLOYEE,
                    State.NEW,
                    CATEGORY,
                    Urgency.AVERAGE,
                    MANAGER);
    private static final Ticket TICKET2 =
            new Ticket(
                    TICKET2_ID,
                    "Ticket2",
                    "Ticket2 description",
                    LocalDate.of(2022, 7, 11),
                    LocalDate.of(2022, 8, 11),
                    ENGINEER,
                    EMPLOYEE,
                    State.APPROVED,
                    CATEGORY,
                    Urgency.AVERAGE,
                    MANAGER);

    private TicketDto ticketDto1;
    private TicketSmallDto ticketSmallDto1;
    private TicketSmallDto ticketSmallDto2;

    private ObjectMapper mapper;

    private TicketConverter ticketConverter;

    @MockBean private TicketService ticketService;

    @MockBean private AccessChecker accessChecker;

    @BeforeEach
    public void initObjects() {
        super.initObjects();

        ticketConverter = context.getBean(TicketConverter.class);

        ticketSmallDto1 = ticketConverter.toSmallDto(2L, Role.ROLE_MANAGER, TICKET1);
        ticketSmallDto2 = ticketConverter.toSmallDto(3L, Role.ROLE_ENGINEER, TICKET2);

        ticketDto1 = ticketConverter.toDto(TICKET1);

        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Test
    public void testGetAllTickets_WithDefaults() throws Exception {
        final Long expectedCount = 2L;
        final int expextedSize = 2;
        when(ticketService.findAllByUser(any(QueryMetadata.class)))
                .thenReturn(
                        new Page<TicketSmallDto>(2L, List.of(ticketSmallDto1, ticketSmallDto2)));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.count", is(expectedCount), Long.class))
                .andExpect(jsonPath("$.entities", hasSize(expextedSize)))
                .andExpect(jsonPath("$.entities[0].name", is("Ticket1")))
                .andExpect(jsonPath("$.entities[0].actions[0].key", is(Action.APPROVE.getKey())))
                .andExpect(
                        jsonPath("$.entities[0].actions[0].label", is(Action.APPROVE.getLabel())))
                .andExpect(jsonPath("$.entities[0].actions[1].key", is(Action.DECLICE.getKey())))
                .andExpect(
                        jsonPath("$.entities[0].actions[1].label", is(Action.DECLICE.getLabel())))
                .andExpect(jsonPath("$.entities[0].actions[2].key", is(Action.CANCEL.getKey())))
                .andExpect(jsonPath("$.entities[0].actions[2].label", is(Action.CANCEL.getLabel())))
                .andExpect(jsonPath("$.entities[1].name", is("Ticket2")))
                .andExpect(
                        jsonPath("$.entities[1].actions[0].key", is(Action.ASSIGN_TO_ME.getKey())))
                .andExpect(
                        jsonPath(
                                "$.entities[1].actions[0].label",
                                is(Action.ASSIGN_TO_ME.getLabel())))
                .andExpect(jsonPath("$.entities[1].actions[1].key", is(Action.CANCEL.getKey())))
                .andExpect(
                        jsonPath("$.entities[1].actions[1].label", is(Action.CANCEL.getLabel())));

        verify(ticketService).findAllByUser(any(QueryMetadata.class));
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void testGetTicketById_HasAccess() throws Exception {
        when(accessChecker.hasAccess(TICKET1_ID)).thenReturn(true);
        when(ticketService.findById(TICKET1_ID)).thenReturn(ticketDto1);

        mockMvc.perform(get("/api/tickets/{id}", TICKET1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(TICKET1_ID), Long.class))
                .andExpect(jsonPath("$.name", is("Ticket1")))
                .andExpect(jsonPath("$.date", is("10/07/2022")))
                .andExpect(jsonPath("$.resolutionDate", is("10/08/2022")))
                .andExpect(jsonPath("$.urgencyId", is(Urgency.AVERAGE.getKey())))
                .andExpect(jsonPath("$.urgency", is(Urgency.AVERAGE.getLabel())))
                .andExpect(jsonPath("$.statusId", is(State.NEW.ordinal())))
                .andExpect(jsonPath("$.status", is("New")))
                .andExpect(jsonPath("$.categoryId", is(CATEGORY_ID), Long.class))
                .andExpect(jsonPath("$.category", is("Application & Services")))
                .andExpect(jsonPath("$.ownerId", is(EMPLOYEE_ID), Long.class))
                .andExpect(jsonPath("$.owner", is("Lenny Leonart")))
                .andExpect(jsonPath("$.description", is("Ticket1 description")))
                .andExpect(jsonPath("$.approverId", is(MANAGER_ID), Long.class))
                .andExpect(jsonPath("$.approver", is("Seymour Skinner")))
                .andExpect(jsonPath("$.assigneeId", is(ENGINEER_ID), Long.class))
                .andExpect(jsonPath("$.assignee", is("Homer Simpson")));

        verify(accessChecker).hasAccess(TICKET1_ID);
        verify(ticketService).findById(TICKET1_ID);
        verifyNoMoreInteractions(accessChecker, ticketService);
    }

    @Test
    public void testGetTicketById_HasNoAccess() throws Exception {
        when(accessChecker.hasAccess(TICKET1_ID)).thenReturn(false);

        mockMvc.perform(get("/api/tickets/{id}", TICKET1_ID)).andExpect(status().isForbidden());

        verify(accessChecker).hasAccess(TICKET1_ID);
        verifyNoMoreInteractions(accessChecker);
    }

    @Test
    @WithMockUser(roles = {"ENGINEER"})
    public void testSave_RoleEngineer_Forbidden() throws Exception {
        when(ticketService.save(any(TicketDto.class))).thenReturn(TICKET1_ID);

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsBytes(ticketDto1)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(ticketService);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    public void testSave_RoleEmployee_Created() throws Exception {
        when(ticketService.save(any(TicketDto.class))).thenReturn(TICKET1_ID);

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsBytes(ticketDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(TICKET1_ID), Long.class));

        verify(ticketService).save(any(TicketDto.class));
        verifyNoMoreInteractions(ticketService);
    }
}
