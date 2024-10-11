package org.edu.fpm.gym.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User{
    private String specialization;
    private Long userId;

    public Trainer(Long userId, String firstName,
                   String lastName, String username,
                   String password, boolean isActive, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }
}
