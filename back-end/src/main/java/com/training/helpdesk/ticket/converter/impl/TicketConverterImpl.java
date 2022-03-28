package com.training.helpdesk.ticket.converter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.category.service.CategoryService;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TicketConverterImpl implements TicketConverter {

    public final UserService userService;
    public final CategoryService categoryService;

    /* Try Builder */
    @Override
    public TicketDto toDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setDate(ticket.getCreatedOn());
        dto.setResolutionDate(ticket.getDesiredResolutionDate());
        dto.setUrgency(ticket.getUrgency().toString());
        dto.setStatus(ticket.getState().toString());
        dto.setCategory(ticket.getCategory().getId());
        dto.setTicketOwnerId(ticket.getOwner().getId());
        dto.setTicketOwner(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
        User approver = ticket.getApprover();
        if (approver != null) {
            dto.setTicketApproverId(approver.getId());
            dto.setTicketApprover(approver.getFirstName() + " " + approver.getLastName());
        }
        User assignee = ticket.getAssignee();
        if (assignee != null) {
            dto.setTicketAssigneeId(assignee.getId());
            dto.setTicketAssignee(assignee.getFirstName() + " " + assignee.getLastName());
        }
        dto.setDescription(ticket.getDescription());
        return dto;
    }

    @Override
    public Page<TicketDto> toDto(Page<Ticket> page) {
        List<TicketDto> dtos = new ArrayList<>();

        dtos = page.getEntities().stream()
            .map(this::toDto)
            .collect(Collectors.toList());

        return new Page<TicketDto>(page.getCount(), dtos);
    }

	@Override
	public Ticket fromDto(TicketDto ticketDto) {
        Ticket ticket = new Ticket();

        ticket.setName(ticketDto.getName());
        ticket.setCreatedOn(ticketDto.getDate());
        ticket.setDesiredResolutionDate(ticketDto.getResolutionDate());
        ticket.setUrgency(Urgency.values()[Integer.valueOf(ticketDto.getUrgency())]);
        ticket.setState(State.values()[Integer.valueOf(ticketDto.getStatus())]);
        ticket.setCategory(categoryService.findById(ticketDto.getCategory()));
        ticket.setOwner(userService.findById(ticketDto.getTicketOwnerId()));
        ticket.setDescription(ticketDto.getDescription());
		return ticket;
	}
}
