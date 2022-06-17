package com.training.helpdesk.feedback.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FeedbackDto implements Serializable {

    private static final long serialVersionUID = -3077173393769611867L;

    private Long userId;
    private Long ticketId;
    private String ticket;

    @NotNull
    @Min(1)
    private Long rate;
    private LocalDateTime date;
    private String text;
}
