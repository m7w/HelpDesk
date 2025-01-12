package com.training.helpdesk.ticket.converter.impl;

import com.training.helpdesk.category.service.CategoryService;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.converter.action.ActionStrategy;
import com.training.helpdesk.ticket.converter.action.ActionStrategyFactory;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketDto.TicketDtoBuilder;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketConverterImpl implements TicketConverter {

    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public TicketSmallDto toSmallDto(Long id, Role role, Ticket ticket) {
        return TicketSmallDto.builder()
                .id(ticket.getId())
                .name(ticket.getName())
                .resolutionDate(ticket.getDesiredResolutionDate())
                .urgency(ticket.getUrgency().getLabel())
                .status(ticket.getState().toString())
                .actions(generateActions(id, role, ticket))
                .build();
    }

    @Override
    public Page<TicketSmallDto> toSmallDto(Long id, Role role, Page<Ticket> page) {
        List<TicketSmallDto> dtos = new ArrayList<>();

        dtos =
                page.getEntities().stream()
                        .map(ticket -> toSmallDto(id, role, ticket))
                        .collect(Collectors.toList());

        return new Page<TicketSmallDto>(page.getCount(), dtos);
    }

    @Override
    public TicketDto toDto(Ticket ticket) {
        TicketDtoBuilder builder = TicketDto.builder();
        builder.id(ticket.getId())
                .name(ticket.getName())
                .date(ticket.getCreatedOn())
                .resolutionDate(ticket.getDesiredResolutionDate())
                .urgencyId(ticket.getUrgency().ordinal())
                .urgency(ticket.getUrgency().getLabel())
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

    private EnumSet<Action> generateActions(Long id, Role role, Ticket ticket) {
        ActionStrategy strategy = ActionStrategyFactory.getStrategy(role);
        return strategy.generateActions(id, ticket);
    }
}
