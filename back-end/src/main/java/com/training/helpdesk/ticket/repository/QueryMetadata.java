package com.training.helpdesk.ticket.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QueryMetadata {

    private int pageNumber;
    private int pageSize;
    private String orderBy;
    private String order;
    private String searchParams;
}
