package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(User user) {
        log.info("Creating user: {}", user);
        userRepository.save(user);
    }

    public String generateUsername(String firstName, String lastName) {
        log.info("Creating login for user: {}", firstName);
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (existsUserByUsername(username)) {
            username = baseUsername + suffix++;
        }
        log.info("Username successfully generated: {}", username);
        return username;
    }

    public void updateUserPassword(String username, String newPassword) {
        var user = findUserByUsername(username);
        if (user != null) {
            log.info("Updating password for user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(newPassword));
            updateUser(user);
            log.info("Password successfully updated for user: {}", user.getUsername());
        }else {
            log.error("User not found for username: {}", username);
        }
    }

    public String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        log.info("Generated password: {}", password);
        return passwordEncoder.encode(password);
    }

    public boolean existsUserByUsername(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        log.info("Checking if user exists with username: {}- {}", username, exists ? "found" : "not found");
        return exists;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
