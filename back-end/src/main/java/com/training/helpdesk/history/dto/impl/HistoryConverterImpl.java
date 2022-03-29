package com.training.helpdesk.history.dto.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.history.converter.HistoryConverter;
import com.training.helpdesk.history.domain.History;
import com.training.helpdesk.history.dto.HistoryDto;

import org.springframework.stereotype.Component;

@Component
public class HistoryConverterImpl implements HistoryConverter {

	@Override
	public HistoryDto toDto(History history) {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setDate(history.getDate());
        historyDto.setUser(history.getUser().getFirstName() + " " + history.getUser().getLastName());
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
}
