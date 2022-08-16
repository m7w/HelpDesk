package com.training.helpdesk.ticket.service;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;

public interface TicketService {

    TicketDto findById(Long id);

    Page<TicketSmallDto> findAllByUser(QueryMetadata queryMetadata);

    Long save(TicketDto ticketDto);

    void update(Long id, TicketDto ticketDto);

    Boolean securityUserHasAccess(Long id);

    Boolean securityUserIsOwner(Long id);
}
