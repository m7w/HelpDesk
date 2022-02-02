package com.training.helpdesk.ticket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @GetMapping("/other")
    @PreAuthorize("hasAnyRole('MANAGER', 'ENGINEER')")
    public ResponseEntity<String> getOtherTickets() {
        return ResponseEntity.ok("Test tickets other");
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<String> getEmployeeTickets() {
        return ResponseEntity.ok("Test tickets employee");
    }
}
