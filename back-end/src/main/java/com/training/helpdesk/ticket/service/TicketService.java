package com.training.helpdesk.ticket.service;

import java.util.List;

import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.user.domain.Role;

public interface TicketService {

    List<TicketDto> flndAllByUser(Long id, Role role, int pageSize, int pageNumber, String order);
}
