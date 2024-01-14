package com.training.helpdesk.ticket.controller;

import java.util.EnumSet;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import com.training.helpdesk.ticket.domain.Page;
import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.dto.TicketDto;
import com.training.helpdesk.ticket.dto.TicketSmallDto;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.ticket.service.TicketService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/tickets")
public class TicketController {

    private static final String VALIDATION_REGEXP = "[ a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]*";

    private final TicketService ticketService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TicketSmallDto>> getTicketsByUser(
            @RequestParam(name = "is_my", defaultValue = "false") boolean isMyFilter,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            @RequestParam(name = "order_by", defaultValue = "default") String orderBy,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @Pattern(regexp = VALIDATION_REGEXP, message = "Search request is not valid.")
            @RequestParam(name = "search", defaultValue = "") String searchParams) {

        return ResponseEntity.ok(ticketService.findAllByUser(new QueryMetadata(isMyFilter, pageNumber, pageSize, orderBy, order, searchParams)));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@accessChecker.hasAccess(#id)")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") Long id) {
        
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<Long> save(@Valid @RequestBody TicketDto ticketDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.save(ticketDto));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("@accessChecker.hasAccess(#id)")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, 
            @Valid @RequestBody TicketDto ticketDto) {

        ticketService.update(id, ticketDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/urgencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnumSet<Urgency>> getUrgencies() {

        return ResponseEntity.ok(EnumSet.allOf(Urgency.class));
    }
}
