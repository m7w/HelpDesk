package com.training.helpdesk.ticket.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -2728884197328628939L;

    private Long count;
    private List<T> entities;
}
