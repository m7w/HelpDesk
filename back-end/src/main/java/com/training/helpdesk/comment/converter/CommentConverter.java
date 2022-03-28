package com.training.helpdesk.comment.converter;

import java.util.List;

import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.dto.CommentDto;

public interface CommentConverter {

    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comments);

    Comment fromDto(CommentDto commentDto);
}
