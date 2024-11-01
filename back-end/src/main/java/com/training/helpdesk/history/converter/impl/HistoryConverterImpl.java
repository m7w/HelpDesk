package com.training.helpdesk.history.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.training.helpdesk.history.converter.HistoryConverter;
import com.training.helpdesk.history.domain.History;
import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class HistoryConverterImpl implements HistoryConverter {

    private final UserService userService;
    private final TicketRepository ticketRepository;

	@Override
	public HistoryDto toDto(History history) {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setTicketId(history.getTicket().getId());
        historyDto.setDate(history.getDate());
        historyDto.setUser(history.getUser().getFirstName() + " " + history.getUser().getLastName());
        historyDto.setUserId(history.getUser().getId());
        historyDto.setAction(history.getAction());
        historyDto.setDescription(history.getDescription());

		return historyDto;
	}

	@Override
	public List<HistoryDto> toDto(List<History> history) {
        return history.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
	}

	@Override
	public History toEntity(HistoryDto historyDto) {
        History history = new History();
        history.setTicket(ticketRepository.getRefById(historyDto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket with id=" + historyDto.getTicketId() + " not found")));
        history.setDate(historyDto.getDate());
        history.setUser(userService.findById(historyDto.getUserId()));
        history.setAction(historyDto.getAction());
        history.setDescription(historyDto.getDescription());

		return history;
	}

}
