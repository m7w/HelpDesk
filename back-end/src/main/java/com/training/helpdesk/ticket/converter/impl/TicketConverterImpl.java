package com.training.helpdesk.ticket.converter.impl;

import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;

import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Override
    public TicketDto toDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setDate(ticket.getCreatedOn());
        dto.setResolutionDate(ticket.getDesiredResolutionDate());
        dto.setUrgency(ticket.getUrgency());
        dto.setStatus(ticket.getState());
        dto.setCategory(ticket.getCategory().getName());
        dto.setTicketOwner(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
        dto.setComment("Comments should be implemented");
        dto.setDescription(ticket.getDescription());
        return dto;
    }
}
