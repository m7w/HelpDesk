package com.training.helpdesk.ticket.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Urgency {
    CRITICAL("Critical"), 
    HIGH("High"), 
    AVERAGE("Average"), 
    LOW("Low");

    private final String urgency;

    Urgency(String urgency) {
        this.urgency = urgency;
    }

    public static List<Integer> ofSubstring(String pattern) {
        return Stream.of(values())
            .filter(u -> u.toString().toLowerCase().contains(pattern.toLowerCase()))
            .map(Urgency::ordinal)
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return urgency;
    }
}
