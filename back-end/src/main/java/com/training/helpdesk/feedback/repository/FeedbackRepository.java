package com.training.helpdesk.feedback.repository;

import java.util.Optional;

import com.training.helpdesk.feedback.domain.Feedback;

public interface FeedbackRepository {

    Optional<Feedback> findByTicketId(Long id);

    Long save(Feedback feedback);
}
