package com.training.helpdesk.ticket.domain;

public enum Urgency {
    CRITICAL("Critical"), 
    HIGH("High"), 
    AVERAGE("Average"), 
    LOW("Low");

    private final String urgency;

    Urgency(String urgency) {
        this.urgency = urgency;
    }

    @Override
    public String toString() {
        return urgency;
    }
}
