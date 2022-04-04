package com.training.helpdesk.ticket;

import com.training.helpdesk.ticket.service.TicketService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AccessChecker {

    private final TicketService ticketService;

    public boolean check(Long id) {
        return ticketService.hasAccess(id);
    }
}