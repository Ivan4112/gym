package org.edu.fpm.gym.utils;

import org.edu.fpm.gym.entity.User;

public class UserUtils {

    public static User getUserInstance(String firstName, String lastName, String username, String password, boolean isActive) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(isActive);
        return user;
    }
}
