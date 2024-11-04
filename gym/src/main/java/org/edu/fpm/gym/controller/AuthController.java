package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.edu.fpm.gym.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.edu.fpm.gym.utils.ApiPaths.AUTH;

@RestController
@RequestMapping(AUTH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login to the system")
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        boolean authenticated = authService.isAuthenticateUser(username, password);

        return authenticated ? ResponseEntity.ok("Login successful") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @Operation(summary = "Change the password for the user")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("username") String username,
                                                 @RequestParam("oldPassword") String oldPassword,
                                                 @RequestParam("newPassword") String newPassword) {

        boolean passwordChanged = authService.changePassword(username, oldPassword, newPassword);
        return passwordChanged ? ResponseEntity.ok("Password changed successfully") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
    }
}
