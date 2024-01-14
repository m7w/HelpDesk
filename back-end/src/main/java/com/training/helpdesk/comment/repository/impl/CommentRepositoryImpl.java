package com.training.helpdesk.comment.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.repository.CommentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private static final String GET_COMMENTS_BY_TICKET = "from Comment where ticket.id = :id order by date desc";
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

	@Override
	public List<Comment> findByTicketId(Long id) {
		return entityManager.createQuery(GET_COMMENTS_BY_TICKET, Comment.class)
            .setParameter(ID, id)
            .getResultList();
	}

    @Override
    public Long save(Comment comment) {
        entityManager.persist(comment);
        return comment.getId();
    }
}
