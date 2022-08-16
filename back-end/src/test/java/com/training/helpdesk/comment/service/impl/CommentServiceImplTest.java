package com.training.helpdesk.comment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.comment.converter.CommentConverter;
import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.comment.repository.CommentRepository;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

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
    private static final CommentDto COMMENT_DTO = new CommentDto(COMMENT_ID, "Syemour Skinner", "New comment",
            LocalDateTime.parse("2022-07-18T15:30:15"), TICKET_ID);


    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentConverter commentConverter;

    @Test
    public void testFindByTicketId() {
        final int expectedSize = 2;
        final List<Comment> comments = List.of(COMMENT, COMMENT);
        final List<CommentDto> dtos = List.of(COMMENT_DTO, COMMENT_DTO);

        when(commentRepository.findByTicketId(TICKET_ID)).thenReturn(comments);
        when(commentConverter.toDto(comments)).thenReturn(dtos);

        final List<CommentDto> actual = commentService.findByTicketId(TICKET_ID);

        assertEquals(expectedSize, actual.size());
        assertEquals(COMMENT_DTO, actual.get(0));
        assertEquals(COMMENT_DTO, actual.get(1));
        verify(commentRepository).findByTicketId(TICKET_ID);
        verify(commentConverter).toDto(comments);
        verifyNoMoreInteractions(commentRepository, commentConverter);
    }

    @Test
    public void testFindById_NotFound() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());
        String expected = "Comment with id=1 not found";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.findById(COMMENT_ID));

        assertEquals(expected, exception.getMessage());
        verify(commentRepository).findById(COMMENT_ID);
        verifyNoMoreInteractions(commentRepository);
        verifyNoInteractions(commentConverter);
    }
}
