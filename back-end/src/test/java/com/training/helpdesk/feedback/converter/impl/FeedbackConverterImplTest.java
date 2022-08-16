package com.training.helpdesk.feedback.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.feedback.domain.Feedback;
import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeedbackConverterImplTest {

    private static final Long USER_ID = 1L;
    private static final Long TICKET_ID = 1L;
    private static final Long CATEGORY_ID = 1L;
    private static final Long FEEDBACK_ID = 5L;
    private static final Long RATE = 5L;
    private static final String FEEDBACK_TEXT = "Good job";
    private static final User USER = new User(USER_ID, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET = new Ticket(TICKET_ID, "Ticket1", "Ticket1 description",
            LocalDate.of(2022, 7, 10), LocalDate.of(2022, 8, 10), 
            USER, USER, State.NEW, CATEGORY, Urgency.AVERAGE, USER);
    private static final Feedback FEEDBACK = new Feedback(FEEDBACK_ID, USER, RATE, 
            LocalDateTime.parse("2022-07-18T15:30:15"), FEEDBACK_TEXT, TICKET);

    @InjectMocks
    private FeedbackConverterImpl feedbackConverter;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Test
    public void testToDto() {
        Feedback expected = FEEDBACK;
        expected.setId(null);

        when(userService.findById(USER_ID)).thenReturn(USER);
        when(ticketRepository.findById(TICKET_ID)).thenReturn(Optional.of(TICKET));

        final FeedbackDto dto = feedbackConverter.toDto(FEEDBACK);

        final Feedback actual = feedbackConverter.toEntity(dto);

        assertEquals(expected, actual);
    }
}
