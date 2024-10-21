package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);
}
