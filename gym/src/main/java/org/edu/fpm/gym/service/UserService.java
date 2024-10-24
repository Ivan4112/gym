package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.security.SecurityCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private SecurityCredential securityCredential;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSecurityCredential(SecurityCredential securityCredential) {
        this.securityCredential = securityCredential;
    }
    public void createUser(User user) {
        user.setUsername(securityCredential.generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(securityCredential.generatePassword());
        userRepository.save(user);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }

    public void updateUserPassword(String username, String password) {
        securityCredential.updatePassword(username, password);
    }
}
