package org.edu.fpm.gym.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityCredential {

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (userService.existsUserByUsername(username)) {
            username = baseUsername + suffix++;
        }

        return username;
    }

    public void updatePassword(String username, String newPassword) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            log.info("Username: " + user.getUsername());
            user.setPassword(newPassword);
            userService.updateUser(user);
        }else {
            log.error("user not found");
        }
    }


    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
