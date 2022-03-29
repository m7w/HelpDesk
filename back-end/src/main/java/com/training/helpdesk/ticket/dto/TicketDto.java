package com.training.helpdesk.ticket.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketDto implements Serializable {

    private static final long serialVersionUID = 4702833855743470890L;
    private static final String VALIDATION_REGEXP = "[ a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]*";

    Long id;

    @Length(max = 100, message = "Ticket name must be maximum 100 characters.")
    @Pattern(regexp = VALIDATION_REGEXP, message = "Ticket name is not valid.")
    String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate date;

    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate resolutionDate;
    Integer urgencyId;
    String urgency;
    Integer statusId;
    String status;
    Long categoryId;
    String category;

    @NotNull
    @Min(1)
    Long ticketOwnerId;
    String ticketOwner;
    Long ticketApproverId;
    String ticketApprover;
    Long ticketAssigneeId;
    String ticketAssignee;

    @Length(max = 500, message = "Description must be maximum 500 characters.")
    @Pattern(regexp = VALIDATION_REGEXP, message = "Description is not valid.")
    String description;
}
