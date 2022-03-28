package com.training.helpdesk.attachment.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.repository.AttachmentRepository;

import org.springframework.stereotype.Repository;

@Repository
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private static final String GET_ATTACMENTS_BY_TICKET = "from Comment where ticket.id = :id";
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<Attachment> findByTicketId(Long id) {
		return entityManager.createQuery(GET_ATTACMENTS_BY_TICKET, Attachment.class)
            .setParameter(ID, id)
            .getResultList();
	}

	@Override
	public Long save(Attachment attachment) {
        entityManager.persist(attachment);
		return attachment.getId();
	}
}
