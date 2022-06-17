package com.training.helpdesk.ticket.converter;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.user.domain.Role;

public interface TicketConverter {

    TicketDto toDto(Ticket ticket);

    TicketSmallDto toSmallDto(Long id, Role role, Ticket ticket);

    Page<TicketSmallDto> toSmallDto(Long id, Role role, Page<Ticket> page);

    Ticket toEntity(TicketDto ticketDto);
}
