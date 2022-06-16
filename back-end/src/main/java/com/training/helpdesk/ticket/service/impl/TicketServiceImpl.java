package com.training.helpdesk.ticket.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.mail.service.MailService;
import com.training.helpdesk.security.SecurityUser;
import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
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
    private final MailService mailService;

    @Override
    @Transactional(readOnly = true)
    public TicketDto findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ticket with id=" + id + " not found"));

        return ticketConverter.toDto(ticket);
	}

    @Override
    @Transactional(readOnly = true)
    public Page<TicketSmallDto> findAllByUser(QueryMetadata queryMetadata) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Role role = getUserRoles(auth).get(0);
        Long id = getUserId(auth);
        Page<Ticket> page = ticketRepository.findAllByUser(id, role, queryMetadata);

        return ticketConverter.toSmallDto(page);
    }
    
	@Override
    @Transactional
	public Long save(TicketDto ticketDto) {
        Ticket ticket = ticketConverter.toEntity(ticketDto);
        
        Long id = ticketRepository.save(ticket);
        return id;
	}

	@Override
    @Transactional
	public void update(Long id, TicketDto ticketDto) {
        Ticket ticket = ticketConverter.toEntity(ticketDto);

        State oldState = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket with id=" + id + " not found"))
            .getState();

        ticket.setId(id);
        ticketRepository.update(ticket);
        mailService.notify(ticket, oldState);
	}

	@Override
	public Boolean hasAccess(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Role role = getUserRoles(auth).get(0);
        Long userId = getUserId(auth);

        return ticketRepository.hasAccess(id, userId, role, new QueryMetadata());
	}

    private List<Role> getUserRoles(Authentication auth) {
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(Role::valueOf)
            .collect(Collectors.toList());
    }
    
    private Long getUserId(Authentication auth) {
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        return principal.getId();
    }
}
