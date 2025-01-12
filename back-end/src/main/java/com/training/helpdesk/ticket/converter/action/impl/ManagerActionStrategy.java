package com.training.helpdesk.ticket.converter.action.impl;

import com.training.helpdesk.ticket.converter.action.ActionStrategy;
import com.training.helpdesk.ticket.domain.Action;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.Role;

import java.util.EnumSet;

public class ManagerActionStrategy implements ActionStrategy {

    @Override
    public EnumSet<Action> generateActions(Long id, Ticket ticket) {
        if (id == ticket.getOwner().getId()
                && (ticket.getState() == State.DRAFT || ticket.getState() == State.DECLINED)) {
            return EnumSet.of(Action.SUBMIT, Action.CANCEL);
        }
        if (ticket.getOwner().getRole() == Role.ROLE_EMPLOYEE && ticket.getState() == State.NEW) {
            return EnumSet.of(Action.APPROVE, Action.DECLICE, Action.CANCEL);
        }
        return EnumSet.noneOf(Action.class);
    }
}
