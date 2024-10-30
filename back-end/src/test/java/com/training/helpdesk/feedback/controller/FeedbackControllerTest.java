package com.training.helpdesk.feedback.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.feedback.service.FeedbackService;
import com.training.helpdesk.ticket.util.AccessChecker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.time.LocalDateTime;

@DisabledInAotMode
public class FeedbackControllerTest extends AbstractControllerTest {

    private static final Long USER_ID = 1L;
    private static final Long TICKET_ID = 1L;
    private static final Long FEEDBACK_ID = 5L;
    private static final Long RATE = 5L;
    private static final String FEEDBACK_TEXT = "Good job";
    private static final String TICKET_NAME = "Ticket1";
    private static final FeedbackDto DTO1 =
            new FeedbackDto(
                    USER_ID,
                    TICKET_ID,
                    TICKET_NAME,
                    RATE,
                    LocalDateTime.parse("2022-07-14T10:00:00"),
                    FEEDBACK_TEXT);

    @MockBean private FeedbackService feedbackService;

    @MockBean private AccessChecker accessChecker;

    @Test
    public void testGetFeedback() throws Exception {
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);
        when(feedbackService.findByTicketId(TICKET_ID)).thenReturn(DTO1);

        mockMvc.perform(get("/api/tickets/{ticketId}/feedback", TICKET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId", is(USER_ID), Long.class))
                .andExpect(jsonPath("$.ticketId", is(TICKET_ID), Long.class))
                .andExpect(jsonPath("$.ticket", is(TICKET_NAME)))
                .andExpect(jsonPath("$.rate", is(5L), Long.class))
                .andExpect(jsonPath("$.date", is("2022-07-14T10:00:00")))
                .andExpect(jsonPath("$.text", is(FEEDBACK_TEXT)));

        verify(feedbackService).findByTicketId(TICKET_ID);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    public void testSave_IsOwner() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        when(feedbackService.save(DTO1)).thenReturn(FEEDBACK_ID);
        when(accessChecker.isOwner(TICKET_ID)).thenReturn(true);

        mockMvc.perform(
                        post("/api/tickets/{ticnetId}/feedback", TICKET_ID)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(DTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(FEEDBACK_ID)));

        verify(feedbackService).save(DTO1);
        verifyNoMoreInteractions(feedbackService);
    }

    @Test
    public void testSave_IsNotOwner() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        when(feedbackService.save(DTO1)).thenReturn(FEEDBACK_ID);
        when(accessChecker.isOwner(TICKET_ID)).thenReturn(false);

        mockMvc.perform(
                        post("/api/tickets/{ticnetId}/feedback", TICKET_ID)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(DTO1)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(feedbackService);
    }
}
