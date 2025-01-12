package com.training.helpdesk.ticket.converter.action.impl;

import com.training.helpdesk.ticket.converter.action.ActionStrategy;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;

import java.util.EnumSet;

public class EngineerActionStrategy implements ActionStrategy {

    @Override
    public EnumSet<Action> generateActions(Long id, Ticket ticket) {
        if (ticket.getState() == State.APPROVED) {
            return EnumSet.of(Action.ASSIGN_TO_ME, Action.CANCEL);
        }
        if (ticket.getState() == State.IN_PROGRESS) {
            return EnumSet.of(Action.DONE);
        }

        return EnumSet.noneOf(Action.class);
    }
}
