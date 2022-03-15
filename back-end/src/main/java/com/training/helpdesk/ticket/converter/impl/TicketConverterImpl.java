package com.training.helpdesk.ticket.converter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Page;
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
        dto.setUrgency(ticket.getUrgency().toString());
        dto.setStatus(ticket.getState().toString());
        dto.setCategory(ticket.getCategory().getName());
        dto.setTicketOwner(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
        dto.setComment("Comments should be implemented");
        dto.setDescription(ticket.getDescription());
        return dto;
    }

    @Override
    public Page<TicketDto> toDto(Page<Ticket> page) {
        List<TicketDto> tickets = new ArrayList<>();

        tickets = page.getEntities().stream()
            .map(this::toDto)
            .collect(Collectors.toList());

        return new Page<TicketDto>(page.getCount(), tickets);
    }
}
