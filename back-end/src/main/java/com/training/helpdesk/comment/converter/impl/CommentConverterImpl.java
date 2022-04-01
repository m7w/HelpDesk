package com.training.helpdesk.comment.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.comment.converter.CommentConverter;
import com.training.helpdesk.comment.domain.Comment;
import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.service.UserService;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommentConverterImpl implements CommentConverter {

    private final UserService userService;
    private final TicketRepository ticketRepository;

	@Override
	public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUser(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        commentDto.setText(comment.getText());
        commentDto.setDate(comment.getDate());
		return commentDto;
	}

	@Override
	public List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
	}

	@Override
	public Comment toEntity(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setUser(userService.findById(commentDto.getUserId()));
        comment.setText(commentDto.getText());
        comment.setDate(commentDto.getDate());
        comment.setTicket(ticketRepository.findById(commentDto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Exception in converter")));
		return comment;
	}
}
