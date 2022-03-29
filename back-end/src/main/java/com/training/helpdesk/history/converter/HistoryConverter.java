package com.training.helpdesk.history.converter;

import java.util.List;

import com.training.helpdesk.history.domain.History;
import com.training.helpdesk.history.dto.HistoryDto;

public interface HistoryConverter {

    public HistoryDto toDto(History history);

    public List<HistoryDto> toDto(List<History> history);
}
