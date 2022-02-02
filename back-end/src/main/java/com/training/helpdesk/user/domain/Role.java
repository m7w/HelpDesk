package com.training.helpdesk.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_EMPLOYEE,
    ROLE_MANAGER,
    ROLE_ENGINEER;

    @Override
    public String getAuthority() {
        return name();
    }
}
