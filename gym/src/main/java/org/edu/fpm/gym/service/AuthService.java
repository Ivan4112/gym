package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean isAuthenticateUser(String username, String password) {
        log.info("Start isAuthenticateUser for username: {}", username);

        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> u.getPassword().equals(password)).orElse(false);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        boolean isChangedPassword = false;
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(oldPassword)) {
            var user = userOptional.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            log.info("Password successfully changed for username: {}", username);
            isChangedPassword = true;
        } else {
            log.warn("Password change failed for username: {}", username);
        }
        return isChangedPassword;
    }
}
