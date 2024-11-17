package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.edu.fpm.gym.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gym/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 3. Login (GET method)
    @Operation(
            summary = "Login to the system",
            description = "Authenticate a user using their username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully login"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error occurred")
    })
    @GetMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "Username of the user", required = true)
            @RequestParam("username") String username,

            @Parameter(description = "Password of the user", required = true)
            @RequestParam("password") String password) {

        boolean authenticated = authService.authenticateUser(username, password);
        if (authenticated) {
//            session.setAttribute("username", username);
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // 4. Change Login (PUT method)
    @Operation(
            summary = "Change the password for the user",
            description = "Allow the user to change their password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed password"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error occurred")
    })
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Parameter(description = "Username of the user", required = true)
            @RequestParam("username") String username,

            @Parameter(description = "Old password", required = true)
            @RequestParam("oldPassword") String oldPassword,

            @Parameter(description = "New password", required = true)
            @RequestParam("newPassword") String newPassword) {

        /*String sessionUsername = (String) session.getAttribute("username");
        if (sessionUsername == null || !sessionUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }*/

        boolean passwordChanged = authService.changePassword(username, oldPassword, newPassword);
        if (passwordChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        }
    }
}
