package com.training.helpdesk.history.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.history.converter.HistoryConverter;
import com.training.helpdesk.history.domain.History;
import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.history.repository.HistoryRepository;
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
public class HistoryServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final Long TICKET_ID = 2L;
    private static final Long CATEGORY_ID = 3L;
    private static final Long HISTORY_ID = 3L;
    private static final User USER = new User(USER_ID, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");
    private static final Category CATEGORY = new Category(CATEGORY_ID, "Application & Services");
    private static final Ticket TICKET = new Ticket(TICKET_ID, "Ticket1", "Ticket1 description",
            LocalDate.of(2022, 7, 10), LocalDate.of(2022, 8, 10),
            USER, USER, State.NEW, CATEGORY, Urgency.AVERAGE, USER);
    private static final History HISTORY = new History(HISTORY_ID, TICKET,
            LocalDateTime.parse("2022-07-18T15:30:15"), "Action", USER, "Description");
    private static final HistoryDto HISTORY_DTO = new HistoryDto(TICKET_ID, 
            LocalDateTime.parse("2022-07-18T15:30:15"), USER_ID, "Seymour Skinner", "Action", "Description");


    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private HistoryConverter historyConverter;

    @Test
    public void testFindByTicketId() {
        final int expectedSize = 2;
        final List<History> historys = List.of(HISTORY, HISTORY);
        final List<HistoryDto> dtos = List.of(HISTORY_DTO, HISTORY_DTO);

        when(historyRepository.findByTicketId(TICKET_ID)).thenReturn(historys);
        when(historyConverter.toDto(historys)).thenReturn(dtos);

        final List<HistoryDto> actual = historyService.findByTicketId(TICKET_ID);

        assertEquals(expectedSize, actual.size());
        assertEquals(HISTORY_DTO, actual.get(0));
        assertEquals(HISTORY_DTO, actual.get(1));
        verify(historyRepository).findByTicketId(TICKET_ID);
        verify(historyConverter).toDto(historys);
        verifyNoMoreInteractions(historyRepository, historyConverter);
    }
}
