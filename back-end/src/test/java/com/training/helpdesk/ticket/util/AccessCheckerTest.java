package com.training.helpdesk.ticket.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.service.TicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccessCheckerTest {

    private static final Long TICKET_ID = 1L;
    private static final Ticket TICKET = new Ticket();

    @BeforeEach
    public void InitObjects() {
        TICKET.setId(TICKET_ID);
    }

    @InjectMocks
    private AccessChecker accessChecker;

    @Mock
    private TicketService ticketService;

    @Test
    public void testHasAccess_True() {
        when(ticketService.securityUserHasAccess(TICKET_ID)).thenReturn(true);

        assertTrue(accessChecker.hasAccess(TICKET_ID));

        verify(ticketService).securityUserHasAccess(TICKET_ID);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void testHasAccess_False() {
        when(ticketService.securityUserHasAccess(TICKET_ID)).thenReturn(false);

        assertFalse(accessChecker.hasAccess(TICKET_ID));

        verify(ticketService).securityUserHasAccess(TICKET_ID);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void testIsOwner_True() {
        when(ticketService.securityUserIsOwner(TICKET_ID)).thenReturn(true);

        assertTrue(accessChecker.isOwner(TICKET_ID));

        verify(ticketService).securityUserIsOwner(TICKET_ID);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void testIsOwner_False() {
        when(ticketService.securityUserIsOwner(TICKET_ID)).thenReturn(false);

        assertFalse(accessChecker.isOwner(TICKET_ID));

        verify(ticketService).securityUserIsOwner(TICKET_ID);
        verifyNoMoreInteractions(ticketService);
    }
}
