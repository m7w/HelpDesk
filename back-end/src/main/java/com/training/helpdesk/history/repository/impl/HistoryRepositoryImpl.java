package com.training.helpdesk.history.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.history.domain.History;
import com.training.helpdesk.history.repository.HistoryRepository;

import org.springframework.stereotype.Repository;

@Repository
public class HistoryRepositoryImpl implements HistoryRepository {

    private static final String GET_HISTORY_BY_TICKET = "from History where ticket.id = :id order by date desc";
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<History> findByTicketId(Long id) {
		return entityManager.createQuery(GET_HISTORY_BY_TICKET, History.class)
            .setParameter(ID, id)
            .getResultList();
	}

	@Override
	public Long save(History history) {
        entityManager.persist(history);
		return history.getId();
	}
}
