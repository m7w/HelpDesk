package com.training.helpdesk.config;

import com.training.helpdesk.security.SecurityUser;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestConfiguration 
public class TestConfig {

    private static final User EMPLOYEE = new User(1L, "Lenny", "Leonart", Role.ROLE_EMPLOYEE, 
            "user1_mogilev@yopmail.com", "password");
    private static final User MANAGER = new User(2L, "Seymour", "Skinner", Role.ROLE_MANAGER,
            "manager1_mogilev@yopmail.com", "password");

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return (username) -> {
            if ("user1_mogilev@yopmail.com".equals(username)) {
                return new SecurityUser(EMPLOYEE);
            } else if ("manager1_mogilev@yopmail.com".equals(username)) {
                return new SecurityUser(MANAGER);
            } else {
                throw new UsernameNotFoundException("User with username=" + username + " not found.");
            }
        };
    }
}
