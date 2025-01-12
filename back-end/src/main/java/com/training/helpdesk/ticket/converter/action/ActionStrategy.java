package com.training.helpdesk.ticket.converter.action;

import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.Ticket;

import java.util.EnumSet;

public interface ActionStrategy {
    EnumSet<Action> generateActions(Long id, Ticket ticket);
}
