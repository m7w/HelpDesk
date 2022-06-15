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
import com.training.helpdesk.ticket.dto.TicketDto.TicketDtoBuilder;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TicketConverterImpl implements TicketConverter {

    public final UserService userService;
    public final CategoryService categoryService;

    @Override
    public TicketSmallDto toSmallDto(Ticket ticket) {
        return TicketSmallDto.builder()
            .id(ticket.getId())
            .name(ticket.getName())
            .resolutionDate(ticket.getDesiredResolutionDate())
            .urgency(ticket.getUrgency().toString())
            .status(ticket.getState().toString())
            .ownerId(ticket.getOwner().getId())
            .ownerRole(ticket.getOwner().getRole())
            .build();
    }

    @Override
    public Page<TicketSmallDto> toSmallDto(Page<Ticket> page) {
        List<TicketSmallDto> dtos = new ArrayList<>();

        dtos = page.getEntities().stream()
            .map(this::toSmallDto)
            .collect(Collectors.toList());

        return new Page<TicketSmallDto>(page.getCount(), dtos);
    }

    @Override
    public TicketDto toDto(Ticket ticket) {
        TicketDtoBuilder builder = TicketDto.builder();
        builder
            .id(ticket.getId())
            .name(ticket.getName())
            .date(ticket.getCreatedOn())
            .resolutionDate(ticket.getDesiredResolutionDate())
            .urgencyId(ticket.getUrgency().ordinal())
            .urgency(ticket.getUrgency().toString())
            .statusId(ticket.getState().ordinal())
            .status(ticket.getState().toString())
            .categoryId(ticket.getCategory().getId())
            .category(ticket.getCategory().getName())
            .ownerId(ticket.getOwner().getId())
            .owner(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName())
            .description(ticket.getDescription());
        User approver = ticket.getApprover();
        if (approver != null) {
            builder.approverId(approver.getId())
                .approver(approver.getFirstName() + " " + approver.getLastName());
        }
        User assignee = ticket.getAssignee();
        if (assignee != null) {
            builder.assigneeId(assignee.getId())
                .assignee(assignee.getFirstName() + " " + assignee.getLastName());
        }

        return builder.build();
    }

	@Override
	public Ticket toEntity(TicketDto ticketDto) {
        Ticket ticket = new Ticket();

        ticket.setId(ticketDto.getId());
        ticket.setName(ticketDto.getName());
        ticket.setCreatedOn(ticketDto.getDate());
        ticket.setDesiredResolutionDate(ticketDto.getResolutionDate());
        ticket.setUrgency(Urgency.values()[ticketDto.getUrgencyId()]);
        ticket.setState(State.values()[ticketDto.getStatusId()]);
        ticket.setCategory(categoryService.findById(ticketDto.getCategoryId()));
        ticket.setOwner(userService.findById(ticketDto.getOwnerId()));
        if (ticketDto.getApproverId() != null) {
            ticket.setApprover(userService.findById(ticketDto.getApproverId()));
        }
        if (ticketDto.getAssigneeId() != null) {
            ticket.setAssignee(userService.findById(ticketDto.getAssigneeId()));
        }
        ticket.setDescription(ticketDto.getDescription());

		return ticket;
	}
}
