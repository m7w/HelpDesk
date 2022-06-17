package com.training.helpdesk.ticket.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum Action {
    CREATE(0, "Create"),
    SUBMIT(1, "Submit"),
    APPROVE(2, "Approve"), 
    DECLICE(3, "Decline"),
    ASSIGN_TO_ME(4, "Assign to Me"),
    DONE(5, "Done"),
    CANCEL(6, "Cancel");

    private final int key;
    private final String label;
}
