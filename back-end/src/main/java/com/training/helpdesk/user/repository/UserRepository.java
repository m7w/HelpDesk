package com.training.helpdesk.user.repository;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> getRefById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}
