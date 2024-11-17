package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public void createUser(User user) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Creating user: {}, transaction id:{}", user, transactionId);
        userRepository.save(user);
        MDC.clear();
    }

    public String generateUsername(String firstName, String lastName) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Creating login for user: {}, transaction id:{}", firstName, transactionId);
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (existsUserByUsername(username)) {
            username = baseUsername + suffix++;
        }
        log.info("Username successfully generated: {}", username);
        MDC.clear();
        return username;
    }

    public void updateUserPassword(String username, String newPassword) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);

        User user = findUserByUsername(username);
        if (user != null) {
            log.info("Updating password for user: {}, transactionId:{}", user.getUsername(), transactionId);
            user.setPassword(newPassword);
            updateUser(user);
            log.info("Password successfully updated for user: {}", user.getUsername());
        }else {
            log.error("User not found for username: {}", username);
        }
        MDC.clear();
    }


    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public boolean existsUserByUsername(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        boolean exists = userRepository.findByUsername(username).isPresent();
        log.info("Checking if user exists with username: {}- {}", username, exists ? "found" : "not found");
        MDC.clear();
        return exists;
    }

    public User findUserByUsername(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Searching user by username: {}, transaction id:{}", username, transactionId);
        MDC.clear();
        return userRepository.findByUsername(username).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
