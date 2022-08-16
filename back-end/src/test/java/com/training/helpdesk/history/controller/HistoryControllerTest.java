package com.training.helpdesk.history.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.history.service.HistoryService;
import com.training.helpdesk.ticket.util.AccessChecker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class HistoryControllerTest extends AbstractControllerTest {

    private static final Long USER_ID = 1L;
    private static final Long TICKET_ID = 2L;
    private static final Long HISTORY_ID = 3L;
    private static final HistoryDto DTO1 = new HistoryDto(TICKET_ID, LocalDateTime.parse("2022-07-14T10:00:00"), 
            USER_ID, "User", "Action", "Description");

    @MockBean
    private HistoryService historyService;

    @MockBean
    private AccessChecker accessChecker;

    @Test
    public void testGetByTicketId() throws Exception {
        final int expectedSize = 2;
        when(historyService.findByTicketId(TICKET_ID)).thenReturn(List.of(DTO1, DTO1));
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);

        mockMvc.perform(get("/api/tickets/{ticketId}/history", TICKET_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(expectedSize)))
            .andExpect(jsonPath("$[0].ticketId", is(TICKET_ID), Long.class))
            .andExpect(jsonPath("$[0].date", is("2022-07-14T10:00:00")))
            .andExpect(jsonPath("$[0].userId", is(USER_ID), Long.class))
            .andExpect(jsonPath("$[0].user", is("User")))
            .andExpect(jsonPath("$[0].action", is("Action")))
            .andExpect(jsonPath("$[0].description", is("Description")))
            .andExpect(jsonPath("$[1].ticketId", is(TICKET_ID), Long.class))
            .andExpect(jsonPath("$[1].date", is("2022-07-14T10:00:00")))
            .andExpect(jsonPath("$[1].userId", is(USER_ID), Long.class))
            .andExpect(jsonPath("$[1].user", is("User")))
            .andExpect(jsonPath("$[1].action", is("Action")))
            .andExpect(jsonPath("$[1].description", is("Description")));

        verify(historyService).findByTicketId(TICKET_ID);
        verifyNoMoreInteractions(historyService);
    }

    @Test
    public void testSave_HasAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        when(historyService.save(DTO1)).thenReturn(HISTORY_ID);
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);

        mockMvc.perform(post("/api/tickets/{ticnetId}/history", TICKET_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(DTO1)))
            .andExpect(status().isCreated())
            .andExpect(content().string(String.valueOf(HISTORY_ID)));

        verify(historyService).save(DTO1);
        verifyNoMoreInteractions(historyService);
    }

    @Test
    public void testSave_HasNoAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        when(historyService.save(DTO1)).thenReturn(HISTORY_ID);
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(false);

        mockMvc.perform(post("/api/tickets/{ticnetId}/history", TICKET_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(DTO1)))
            .andExpect(status().isForbidden());

        verifyNoInteractions(historyService);
    }
}
