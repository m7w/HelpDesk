package com.training.helpdesk.comment.repository;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.comment.domain.Comment;

public interface CommentRepository {

    Optional<Comment> findById(Long id);

    List<Comment> findByTicketId(Long id);

    Long save(Comment comment);
}
