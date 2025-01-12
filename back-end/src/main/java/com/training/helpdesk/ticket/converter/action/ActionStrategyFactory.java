package com.training.helpdesk.ticket.converter.action;

import com.training.helpdesk.ticket.converter.action.impl.EmployeeActionStrategy;
import com.training.helpdesk.ticket.converter.action.impl.EngineerActionStrategy;
import com.training.helpdesk.ticket.converter.action.impl.ManagerActionStrategy;
import com.training.helpdesk.user.domain.Role;

public class ActionStrategyFactory {
    public static ActionStrategy getStrategy(Role role) {
        switch (role) {
            case ROLE_EMPLOYEE:
                return new EmployeeActionStrategy();
            case ROLE_MANAGER:
                return new ManagerActionStrategy();
            case ROLE_ENGINEER:
                return new EngineerActionStrategy();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
