package com.training.helpdesk.user.service;

import java.util.List;

import com.training.helpdesk.user.domain.User;

public interface UserService {

    List<User> findAll();

    User findById(Long id);
}
