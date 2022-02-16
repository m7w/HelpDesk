package com.training.helpdesk.ticket.converter;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;

public interface TicketConverter {

    TicketDto toDto(Ticket ticket);
}
