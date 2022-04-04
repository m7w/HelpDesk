package com.training.helpdesk.feedback.service.impl;

import com.training.helpdesk.feedback.converter.FeedbackConverter;
import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.feedback.repository.FeedbackRepository;
import com.training.helpdesk.feedback.service.FeedbackService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackConverter feedbackConverter;

	@Override
    @Transactional(readOnly = true)
	public FeedbackDto findByTicketId(Long id) {
		return feedbackConverter.toDto(feedbackRepository.findByTicketId(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback for ticket with id=" + id + " not found")));
	}

	@Override
    @Transactional
	public Long save(FeedbackDto feedbackDto) {
		return feedbackRepository.save(feedbackConverter.toEntity(feedbackDto));
	}
}
