package com.training.helpdesk.ticket.repository;

import java.util.Optional;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.Role;

public interface TicketRepository {

    Optional<Ticket> findById(Long id);

    Page<Ticket> findAllByUser(Long id, Role role, QueryMetadata queryMetadata);

    Long save(Ticket ticket);

    Boolean hasAccess(Long ticketId, Long userId, Role role, QueryMetadata queryMetadata);
}
