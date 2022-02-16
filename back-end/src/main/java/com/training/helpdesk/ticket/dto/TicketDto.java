package com.training.helpdesk.ticket.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Urgency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketDto implements Serializable {

    private static final long serialVersionUID = 4702833855743470890L;

    Long id;
    String name;
    LocalDate date;
    LocalDate resolutionDate;
    Urgency urgency;
    State status;
    String category;
    String ticketOwner;
    String comment;
    String description;

}
