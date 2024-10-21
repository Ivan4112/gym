package org.edu.fpm.gym.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }
}
