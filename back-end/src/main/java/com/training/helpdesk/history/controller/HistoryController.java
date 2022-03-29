package com.training.helpdesk.history.controller;

import java.util.List;

import com.training.helpdesk.history.dto.HistoryDto;
import com.training.helpdesk.history.service.HistoryService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping(value = "/{ticketId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HistoryDto>> getByTicketId(@PathVariable("ticketId") Long ticketId) {

        return ResponseEntity.ok(historyService.findByTicketId(ticketId));
    }
}
