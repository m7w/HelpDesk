package com.training.helpdesk.ticket.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.security.SecurityUser;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.service.TicketService;
import com.training.helpdesk.user.domain.Role;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketDto>> getEmployeeTickets(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @RequestParam(name = "sort", defaultValue = "urgency.asc") String order,
            Authentication auth) {

        List<String> roles = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        Role role = Role.valueOf(roles.get(0));
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        Long id = principal.getId();

        List<TicketDto> tickets = ticketService.flndAllByUser(id, role, pageNumber, pageSize, order);

        return ResponseEntity.ok(tickets);
    }
}
