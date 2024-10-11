package org.edu.fpm.gym.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
