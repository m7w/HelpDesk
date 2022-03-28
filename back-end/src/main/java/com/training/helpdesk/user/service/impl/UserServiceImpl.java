package com.training.helpdesk.user.service.impl;

import java.util.List;

import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.repository.UserRepository;
import com.training.helpdesk.user.service.UserService;

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
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id=" + id + " not found"));
    }
}
