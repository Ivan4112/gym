package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDaoImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public User save(User user) {
        try {
            log.info("Saving user: {}", user.getUsername());
            entityManager.persist(user);
            log.info("Successfully saved user: {}", user.getUsername());
            return user;
        } catch (Exception e) {
            log.error("Failed to save user: {}", e.getMessage(), e);
            throw e;
        }
    }
}
