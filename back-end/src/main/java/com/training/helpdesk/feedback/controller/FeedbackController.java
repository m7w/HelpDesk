package com.training.helpdesk.feedback.controller;

import com.training.helpdesk.feedback.dto.FeedbackDto;
import com.training.helpdesk.feedback.service.FeedbackService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping(value = "/{ticketId}/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@accessChecker.hasAccess(#ticketId)")
    public ResponseEntity<FeedbackDto> getFeedback(@PathVariable("ticketId") Long ticketId) {

        return ResponseEntity.ok(feedbackService.findByTicketId(ticketId));
    }

    @PostMapping(value = "/{ticketId}/feedback")
    @PreAuthorize("@accessChecker.isOwner(#ticketId)")
    public ResponseEntity<Long> save(@PathVariable("ticketId") Long ticketId,
            @RequestBody FeedbackDto feedbackDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.save(feedbackDto));
    }
}
