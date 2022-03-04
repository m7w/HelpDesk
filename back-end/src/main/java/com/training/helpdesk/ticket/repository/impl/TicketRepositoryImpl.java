package com.training.helpdesk.ticket.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.ticket.util.QueryBuilder;
import com.training.helpdesk.user.domain.Role;

import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;

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
}
