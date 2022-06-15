package com.training.helpdesk.feedback.converter.impl;

import com.training.helpdesk.feedback.converter.FeedbackConverter;
import com.training.helpdesk.feedback.domain.Feedback;
import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.service.UserService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FeedbackConverterImpl implements FeedbackConverter {

    private final UserService userService;
    private final TicketRepository ticketRepository;

	@Override
	public FeedbackDto toDto(Feedback feedback) {
        return FeedbackDto.builder()
            .userId(feedback.getUser().getId())
            .ticketId(feedback.getTicket().getId())
            .ticket(feedback.getTicket().getName())
            .rate(feedback.getRate())
            .date(feedback.getDate())
            .text(feedback.getText())
            .build();
	}

	@Override
	public Feedback toEntity(FeedbackDto feedbackDto) {
        return Feedback.builder()
            .user(userService.findById(feedbackDto.getUserId()))
            .rate(feedbackDto.getRate())
            .date(feedbackDto.getDate())
            .text(feedbackDto.getText())
            .ticket(ticketRepository.findById(feedbackDto.getTicketId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Ticket with id=" + feedbackDto.getTicketId() + " not found")))
            .build();
	}
}
