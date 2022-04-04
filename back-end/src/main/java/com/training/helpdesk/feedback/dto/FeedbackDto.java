package com.training.helpdesk.feedback.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FeedbackDto {

    private Long userId;
    private Long ticketId;
    private String ticket;
    private Long rate;
    private LocalDateTime date;
    private String text;
}
