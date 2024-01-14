package com.training.helpdesk.comment.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class CommentConverterImplTest {

    private static final Long USER_ID = 1L;
    private static final Long CATEGORY_ID = 1L;
    private static final Long TICKET_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    private static final User USER = new User(USER_ID, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET = new Ticket(TICKET_ID, "Ticket1", "Ticket1 description",
            LocalDate.of(2022, 7, 10), LocalDate.of(2022, 8, 10), 
            USER, USER, State.NEW, CATEGORY, Urgency.AVERAGE, USER);
    private static final Comment COMMENT = new Comment(COMMENT_ID, USER, "New comment", 
            LocalDateTime.parse("2022-07-18T15:30:15"), TICKET);

    @InjectMocks
    private CommentConverterImpl commentConverter;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Test
    public void testToDto() {
        Comment expected = COMMENT;
        expected.setId(null);

        when(userService.findById(USER_ID)).thenReturn(USER);
        when(ticketRepository.getRefById(TICKET_ID)).thenReturn(Optional.of(TICKET));

        CommentDto dto = commentConverter.toDto(COMMENT);

        Comment actual = commentConverter.toEntity(dto);

        assertEquals(expected, actual);
    }
}
