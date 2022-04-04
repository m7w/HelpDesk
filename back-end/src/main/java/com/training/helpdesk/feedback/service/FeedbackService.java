package com.training.helpdesk.feedback.service;

import com.training.helpdesk.feedback.dto.FeedbackDto;

public interface FeedbackService {

    FeedbackDto findByTicketId(Long id);

    Long save(FeedbackDto feedbackDto);
}
