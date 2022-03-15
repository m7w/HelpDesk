package com.training.helpdesk.ticket.service.impl;

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

        Role role = getUserRoles().get(0);
        Long id = getUserId();
        Page<Ticket> page = ticketRepository.findAllByUser(id, role, queryMetadata);

        return ticketConverter.toDto(page);
    }
    
    private List<Role> getUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(Role::valueOf)
            .collect(Collectors.toList());
    }
    
    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        return principal.getId();
    }
}
