package com.training.helpdesk.ticket.converter.action.impl;

import com.training.helpdesk.ticket.converter.action.ActionStrategy;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;

import java.util.EnumSet;

public class EmployeeActionStrategy implements ActionStrategy {

    @Override
    public EnumSet<Action> generateActions(Long id, Ticket ticket) {

        if (ticket.getState() == State.DRAFT || ticket.getState() == State.DECLINED) {
            return EnumSet.of(Action.SUBMIT, Action.CANCEL);
        }
        return EnumSet.noneOf(Action.class);
    }
}
