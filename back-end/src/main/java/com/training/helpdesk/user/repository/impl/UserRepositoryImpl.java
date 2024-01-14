package com.training.helpdesk.user.repository.impl;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.repository.UserRepository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String FIND_ALL_USERS = "from User";
    private static final String FIND_BY_EMAIL = "from User where email = :email";
    private static final String FIND_BY_ROLE = "from User where role = :role";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";

    @PersistenceContext private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(FIND_ALL_USERS, User.class).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> getRefById(Long id) {
        return Optional.ofNullable(entityManager.getReference(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entityManager
                .createQuery(FIND_BY_EMAIL, User.class)
                .setParameter(EMAIL, email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<User> findByRole(Role role) {
        return entityManager
                .createQuery(FIND_BY_ROLE, User.class)
                .setParameter(ROLE, role)
                .getResultList();
    }
}
