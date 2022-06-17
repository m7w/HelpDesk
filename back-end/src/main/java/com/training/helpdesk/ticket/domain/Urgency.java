package com.training.helpdesk.ticket.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum Urgency {
    CRITICAL(0, "Critical"), 
    HIGH(1, "High"), 
    AVERAGE(2, "Average"), 
    LOW(3, "Low");

    private final int key;
    private final String label;

    public static List<Integer> ofSubstring(String pattern) {
        return Stream.of(values())
            .filter(v -> v.toString().toLowerCase().contains(pattern.toLowerCase()))
            .map(Urgency::ordinal)
            .collect(Collectors.toList());
    }
}
