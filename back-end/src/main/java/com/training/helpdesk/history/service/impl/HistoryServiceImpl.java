package com.training.helpdesk.history.service.impl;

import java.util.List;

import com.training.helpdesk.history.converter.HistoryConverter;
import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.history.repository.HistoryRepository;
import com.training.helpdesk.history.service.HistoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryConverter historyConverter;

	@Override
    @Transactional(readOnly = true)
	public List<HistoryDto> findByTicketId(Long id) {
		return historyConverter.toDto(historyRepository.findByTicketId(id));
	}

	@Override
    @Transactional
	public Long save(HistoryDto historyDto) {
		return historyRepository.save(historyConverter.toEntity(historyDto));
	}
}
