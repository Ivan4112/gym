package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDaoImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }
}
