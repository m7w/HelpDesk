package com.training.helpdesk.feedback.repository.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.feedback.domain.Feedback;
import com.training.helpdesk.feedback.repository.FeedbackRepository;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {

    private static final String FIND_BY_TICKET = "from Feedback where ticket.id = :id";
    private static final String ID = "id";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
	public Optional<Feedback> findByTicketId(Long id) {
		return entityManager.createQuery(FIND_BY_TICKET, Feedback.class)
                .setParameter(ID, id)
                .getResultStream().findFirst();
	}

	@Override
	public Long save(Feedback feedback) {
        entityManager.persist(feedback);
		return feedback.getId();
	}
}
