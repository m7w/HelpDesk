package com.training.helpdesk.ticket.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.ticket.converter.TicketConverter;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.ticket.service.TicketService;
import com.training.helpdesk.user.domain.Role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketConverter ticketConverter;

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> flndAllByUser(Long id, Role role, int pageNumber, int pageSize, String combinedOrder) {

        String[] combinedOrderArray = combinedOrder.split("\\.");
        String orderBy = combinedOrderArray[0];
        String order = combinedOrderArray[1];
        List<TicketDto> tickets = new ArrayList<>();
        tickets = ticketRepository.findAllByUser(id, role, pageNumber, pageSize, orderBy, order)
            .stream()
            .map(ticketConverter::toDto)
            .collect(Collectors.toList());

        return tickets;
    }
}
