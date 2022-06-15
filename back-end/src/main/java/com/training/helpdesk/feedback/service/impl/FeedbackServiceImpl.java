package com.training.helpdesk.feedback.service.impl;

import com.training.helpdesk.feedback.converter.FeedbackConverter;
import com.training.helpdesk.feedback.domain.Feedback;
import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.feedback.repository.FeedbackRepository;
import com.training.helpdesk.feedback.service.FeedbackService;
import com.training.helpdesk.mail.MailDetails;
import com.training.helpdesk.mail.service.MailService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final MailService mailService;
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
        Feedback feedback = feedbackConverter.toEntity(feedbackDto);
		Long id = feedbackRepository.save(feedback);
        mailService.sendMail(7, "Feedback was provided",
                new MailDetails(feedback.getTicket().getAssignee(), feedback.getTicket().getId()));
        return id;
	}
}
