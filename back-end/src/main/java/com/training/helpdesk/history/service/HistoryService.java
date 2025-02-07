package com.training.helpdesk.history.service;

import java.util.List;

import com.training.helpdesk.history.dto.HistoryDto;

public interface HistoryService {

    List<HistoryDto> findByTicketId(Long id);

    Long save(HistoryDto historyDto);
}
