package com.training.helpdesk.ticket.util;

import com.training.helpdesk.ticket.service.TicketService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessChecker {

    private final TicketService ticketService;

    public boolean hasAccess(Long id) {
        return ticketService.securityUserHasAccess(id);
    }

    public boolean isOwner(Long id) {
        return ticketService.securityUserIsOwner(id);
    }
}
