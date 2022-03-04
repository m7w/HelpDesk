package com.training.helpdesk.ticket.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.security.SecurityUser;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.ticket.service.TicketService;
import com.training.helpdesk.user.domain.Role;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketConverter ticketConverter;

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDto> flndAllByUser(QueryMetadata queryMetadata) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        Role role = Role.valueOf(roles.get(0));
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        Long id = principal.getId();

        Page<Ticket> page = ticketRepository.findAllByUser(id, role, queryMetadata);
        List<TicketDto> tickets = new ArrayList<>();
        tickets = page.getEntities()
            .stream()
            .map(ticketConverter::toDto)
            .collect(Collectors.toList());

        return new Page<TicketDto>(page.getCount(), tickets);
    }
}
