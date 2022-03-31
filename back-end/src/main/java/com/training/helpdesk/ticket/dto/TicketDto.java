package com.training.helpdesk.ticket.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TicketDto implements Serializable {

    private static final long serialVersionUID = 4702833855743470890L;
    private static final String VALIDATION_REGEXP = "[ a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]*";

    private Long id;

    @Length(max = 100, message = "Ticket name must be maximum 100 characters.")
    @Pattern(regexp = VALIDATION_REGEXP, message = "Ticket name is not valid.")
    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate resolutionDate;
    private Integer urgencyId;
    private String urgency;
    private Integer statusId;
    private String status;
    private Long categoryId;
    private String category;

    @NotNull
    @Min(1)
    private Long ownerId;
    private String owner;
    private Long approverId;
    private String approver;
    private Long assigneeId;
    private String assignee;

    @Length(max = 500, message = "Description must be maximum 500 characters.")
    @Pattern(regexp = VALIDATION_REGEXP, message = "Description is not valid.")
    private String description;
}
