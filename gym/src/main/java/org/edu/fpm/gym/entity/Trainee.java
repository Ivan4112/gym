package org.edu.fpm.gym.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Trainee extends User{
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(Long userId, String firstName,
                   String lastName, String username,
                   String password, boolean isActive,
                   LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "userId=" + userId +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", isActive='" + isActive() + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
