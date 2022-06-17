package com.training.helpdesk.ticket.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.helpdesk.ticket.domain.Action;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TicketSmallDto implements Serializable {

    private static final long serialVersionUID = 4702833855743470889L;

    private final Long id;
    private final String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private final LocalDate resolutionDate;
    private final String urgency;
    private final String status;
    private final EnumSet<Action> actions;
}

