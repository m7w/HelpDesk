package com.training.helpdesk.comment.controller;

import java.util.List;

import javax.validation.Valid;

import com.training.helpdesk.comment.dto.CommentDto;
import com.training.helpdesk.comment.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{ticketId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDto>> getByTicketId(@PathVariable("ticketId") Long ticketId) {

        return ResponseEntity.ok(commentService.findByTicketId(ticketId));
    }

    @PostMapping(value = "/{ticketId}/comments")
    public ResponseEntity<Long> save(@PathVariable("ticketId") Long ticketId,
            @Valid @RequestBody CommentDto commentDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentDto));
    }
}
