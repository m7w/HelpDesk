package com.training.helpdesk.ticket.domain;

public enum State {
    DRAFT("Draft"), 
    NEW("New"), 
    APPROVED("Approved"), 
    DECLINED("Declined"), 
    IN_PROGRESS("In Progress"), 
    DONE("Done"), 
    CANCELED("Canceled");

    private final String state;

    State(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
