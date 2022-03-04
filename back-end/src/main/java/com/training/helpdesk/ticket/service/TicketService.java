package com.training.helpdesk.ticket.service;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;

public interface TicketService {

    Page<TicketDto> flndAllByUser(QueryMetadata queryMetadata);
}
