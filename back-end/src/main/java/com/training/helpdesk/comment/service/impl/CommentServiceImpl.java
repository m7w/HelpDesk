package com.training.helpdesk.comment.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.helpdesk.comment.converter.CommentConverter;
import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.comment.repository.CommentRepository;
import com.training.helpdesk.comment.service.CommentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

	@Override
    @Transactional(readOnly = true)
	public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id=" + id + " not found"));

        return commentConverter.toDto(comment);
	}

	@Override
    @Transactional(readOnly = true)
	public List<CommentDto> findByTicketId(Long id) {
		return commentConverter.toDto(commentRepository.findByTicketId(id));
	}

	@Override
    @Transactional
	public Long save(CommentDto commentDto) {
        Comment comment = commentConverter.toEntity(commentDto);
        return commentRepository.save(comment);
	}
}
