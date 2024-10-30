package com.training.helpdesk.comment.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.comment.service.CommentService;
import com.training.helpdesk.ticket.util.AccessChecker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.time.LocalDateTime;
import java.util.List;

@DisabledInAotMode
public class CommentControllerTest extends AbstractControllerTest {

    private static final Long TICKET_ID = 1L;
    private static final Long USER1_ID = 1L;
    private static final Long USER2_ID = 2L;
    private static final Long COMMENT_ID = 3L;
    private static final CommentDto DTO1 =
            new CommentDto(
                    USER1_ID,
                    "Lenny",
                    "Lenny message",
                    LocalDateTime.parse("2022-04-03T10:15:30"),
                    TICKET_ID);
    private static final CommentDto DTO2 =
            new CommentDto(
                    USER2_ID,
                    "Homer",
                    "Homer message",
                    LocalDateTime.parse("2022-04-03T11:15:30"),
                    TICKET_ID);

    @MockBean private CommentService commentService;

    @MockBean private AccessChecker accessChecker;

    @Test
    public void testGetByTicketId() throws Exception {
        final int expectedSize = 2;
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);
        when(commentService.findByTicketId(TICKET_ID)).thenReturn(List.of(DTO1, DTO2));

        mockMvc.perform(get("/api/tickets/{ticketId}/comments", TICKET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(expectedSize)))
                .andExpect(jsonPath("$[0].userId", is(USER1_ID), Long.class))
                .andExpect(jsonPath("$[0].user", is("Lenny")))
                .andExpect(jsonPath("$[0].text", is("Lenny message")))
                .andExpect(jsonPath("$[0].date", is("2022-04-03T10:15:30")))
                .andExpect(jsonPath("$[0].ticketId", is(TICKET_ID), Long.class))
                .andExpect(jsonPath("$[1].userId", is(USER2_ID), Long.class))
                .andExpect(jsonPath("$[1].user", is("Homer")))
                .andExpect(jsonPath("$[1].text", is("Homer message")))
                .andExpect(jsonPath("$[1].date", is("2022-04-03T11:15:30")))
                .andExpect(jsonPath("$[1].ticketId", is(TICKET_ID), Long.class));

        verify(commentService).findByTicketId(TICKET_ID);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testSave() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);
        when(commentService.save(DTO1)).thenReturn(COMMENT_ID);

        mockMvc.perform(
                        post("/api/tickets/{ticnetId}/comments", TICKET_ID)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsBytes(DTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(COMMENT_ID)));

        verify(commentService).save(DTO1);
        verifyNoMoreInteractions(commentService);
    }
}
