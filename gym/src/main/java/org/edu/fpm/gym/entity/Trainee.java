package org.edu.fpm.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
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

    public Trainee() {
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
