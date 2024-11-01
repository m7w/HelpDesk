package com.training.helpdesk.user.service.impl;

import java.util.List;

import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.repository.UserRepository;
import com.training.helpdesk.user.service.UserService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usersCache")
    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id=" + id + " not found"));
    }

    @Override
    public User getRefById(Long id) {
        return userRepository
                .getRefById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("User with id=" + id + " not found"));
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
