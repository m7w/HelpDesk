package com.training.helpdesk.ticket.converter;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;

public interface TicketConverter {

    TicketDto toDto(Ticket ticket);

    TicketSmallDto toSmallDto(Ticket ticket);

    Page<TicketSmallDto> toSmallDto(Page<Ticket> page);

    Ticket toEntity(TicketDto ticketDto);
}
