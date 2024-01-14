package com.training.helpdesk.ticket.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.ticket.util.QueryBuilder;
import com.training.helpdesk.user.domain.Role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    
    private static final String GET_TICKET = "from Ticket t join fetch t.owner join fetch t.category where t.id = :ticketId";

    private static final String ID = "id";
    private static final String TICKET_ID = "ticketId";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ticket> findById(Long id) {
        return entityManager.createQuery(GET_TICKET, Ticket.class)
                .setParameter(TICKET_ID, id)
                .getResultStream().findFirst();
    }

    @Override
    public Page<Ticket> findAllByUser(Long id, Role role, QueryMetadata queryMetadata) {

        Long count = entityManager.createQuery(QueryBuilder.buildCountQuery(role, queryMetadata),
                Long.class)
            .setParameter(ID, id)
            .getSingleResult();

        List<Ticket> tickets = entityManager.createQuery(QueryBuilder.buildPlainQuery(role, queryMetadata),
                Ticket.class)
            .setParameter(ID, id)
            .setFirstResult(queryMetadata.getPageNumber() * queryMetadata.getPageSize())
            .setMaxResults(queryMetadata.getPageSize())
            .getResultList();

        return new Page<Ticket>(count, tickets);
    }

    @Override
    public Long save(Ticket ticket) {
        entityManager.persist(ticket);
        return ticket.getId();
    }

    @Override
    public Ticket update(Ticket ticket) {
        return entityManager.merge(ticket);
    }

	@Override
	public Boolean securityUserHasAccess(Long ticketId, Long userId, Role role, QueryMetadata queryMetadata) {
        List<Ticket> tickets = entityManager.createQuery(QueryBuilder.buildCheckAccessQuery(role, queryMetadata), Ticket.class)
            .setParameter(ID, userId)
            .setParameter(TICKET_ID, ticketId)
            .getResultList();

        if (tickets.isEmpty()) {
            return false;
        }
		return true;
	}
}
