package com.training.helpdesk.ticket.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class QueryMetadata {

    private boolean isMyFilter;
    private int pageNumber;
    private int pageSize;
    private String orderBy;
    private String order;
    private String searchParams;
}
