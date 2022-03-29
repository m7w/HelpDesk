package com.training.helpdesk.history.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {

    LocalDateTime date;
    String user;
    String action;
    String description;
}
