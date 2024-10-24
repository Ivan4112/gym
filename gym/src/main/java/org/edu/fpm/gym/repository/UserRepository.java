package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
