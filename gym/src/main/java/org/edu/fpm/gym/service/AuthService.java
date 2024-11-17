package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public boolean authenticateUser(String username, String password) {
        String transactionId = generateTransactionId();
        MDC.put("authenticationTransactionId", transactionId);

        log.info("Start authenticateUser for username: {}", username);

        Optional<User> user = userRepository.findByUsername(username);
        boolean isAuthenticated = user.map(u -> u.getPassword().equals(password)).orElse(false);

        if(isAuthenticated){
            MDC.put("authenticationTransactionId", transactionId);
        } else {
            MDC.put("authenticationTransactionId", null);
        }
        MDC.clear();
        return isAuthenticated;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        String transactionId = generateTransactionId();
        MDC.put("changePasswordTransactionId", transactionId);

        Optional<User> userOptional = userRepository.findByUsername(username);
        boolean isChangedPassword = false;
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(oldPassword)) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            log.info("Password successfully changed for username: {}", username);
            isChangedPassword = true;
        } else {
            log.warn("Password change failed for username: {}", username);
        }
        MDC.clear();
        return isChangedPassword;
    }
}
