package com.training.helpdesk.feedback.converter;

import com.training.helpdesk.feedback.domain.Feedback;
import com.training.helpdesk.feedback.dto.FeedbackDto;

public interface FeedbackConverter {

    FeedbackDto toDto(Feedback feedback);

    Feedback toEntity(FeedbackDto feedbackDto);
}
