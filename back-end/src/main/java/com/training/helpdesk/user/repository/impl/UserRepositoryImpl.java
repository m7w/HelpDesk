package com.training.helpdesk.user.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.repository.UserRepository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String FIND_ALL_USERS = "from User";
    private static final String FIND_BY_EMAIL = "from User where email = :email";
    private static final String EMAIL = "email";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(FIND_ALL_USERS, User.class).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery(FIND_BY_EMAIL, User.class)
                .setParameter(EMAIL, email)
                .getResultStream().findFirst();
    }
}
