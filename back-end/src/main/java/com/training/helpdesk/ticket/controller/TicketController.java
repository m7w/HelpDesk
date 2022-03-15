package com.training.helpdesk.ticket.controller;

import javax.validation.constraints.Pattern;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.service.TicketService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/tickets")
public class TicketController {

    private static final String VALIDATION_REGEXP = "[a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]*";

    private final TicketService ticketService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TicketDto>> getTickets(
            @RequestParam(name = "is_my", defaultValue = "false") boolean isMyFilter,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @RequestParam(name = "order_by", defaultValue = "default") String orderBy,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @Pattern(regexp = VALIDATION_REGEXP, message = "Search request is not valid.")
            @RequestParam(name = "search", defaultValue = "") String searchParams) {


        return ResponseEntity.ok(ticketService.flndAllByUser(new QueryMetadata(isMyFilter, pageNumber, pageSize, orderBy, order, searchParams)));
    }
}
