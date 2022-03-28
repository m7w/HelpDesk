package com.training.helpdesk.comment.service;

import java.util.List;

import com.training.helpdesk.comment.dto.CommentDto;

public interface CommentService {

    CommentDto findById(Long id);

    List<CommentDto> findByTicketId(Long id);

    Long save(CommentDto commentDto);
}
