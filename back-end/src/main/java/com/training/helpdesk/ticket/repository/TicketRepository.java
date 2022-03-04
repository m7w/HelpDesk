package com.training.helpdesk.ticket.repository;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.Role;

public interface TicketRepository {

    Page<Ticket> findAllByUser(Long id, Role role, QueryMetadata queryMetadata);
}
