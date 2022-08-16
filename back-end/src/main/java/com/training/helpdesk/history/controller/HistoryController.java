package com.training.helpdesk.history.controller;

import java.util.List;

import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.history.service.HistoryService;

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
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping(value = "/{ticketId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@accessChecker.hasAccess(#ticketId)")
    public ResponseEntity<List<HistoryDto>> getByTicketId(@PathVariable("ticketId") Long ticketId) {

        return ResponseEntity.ok(historyService.findByTicketId(ticketId));
    }

    @PostMapping(value = "/{ticketId}/history")
    @PreAuthorize("@accessChecker.hasAccess(#ticketId)")
    public ResponseEntity<Long> save(@PathVariable("ticketId") Long ticketId, 
            @RequestBody HistoryDto historyDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.save(historyDto));
    }
}
