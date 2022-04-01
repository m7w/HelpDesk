package com.training.helpdesk.history.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {

    private Long ticketId;
    private LocalDateTime date;
    private Long userId;
    private String user;
    private String action;
    private String description;
}
