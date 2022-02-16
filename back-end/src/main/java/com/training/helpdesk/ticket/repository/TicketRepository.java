package com.training.helpdesk.ticket.repository;

import java.util.List;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.Role;

public interface TicketRepository {

    List<Ticket> findAllByUser(Long id, Role role, int pageNumber, int pageSize, String orderBy, String order);
}
