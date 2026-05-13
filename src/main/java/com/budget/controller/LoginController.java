package com.budget.controller;

import com.budget.models.User;
import com.budget.service.AuthService;

/**

 * Handles authentication use-cases for the login screen (no JavaFX dependencies).
 */
public class LoginController {

    private final AuthService authService;

    public LoginController() {
        this(new AuthService());
    }

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public AuthResult login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            return AuthResult.failure("Please fill in all fields");
        }
        String u = username.trim();
        if (authService.login(u, password)) {
            User user = authService.getCurrentUser();
            return AuthResult.success(user);
        }
        return AuthResult.failure("Invalid username or password");
    }

    /** Immutable result of a login attempt. */
    public static final class AuthResult {
        private final boolean success;
        private final User user;
        private final String message;

        private AuthResult(boolean success, User user, String message) {
            this.success = success;
            this.user = user;
            this.message = message;
        }

        public static AuthResult success(User user) {
            return new AuthResult(true, user, null);
        }

        public static AuthResult failure(String message) {
            return new AuthResult(false, null, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public User getUser() {
            return user;
        }

        public String getMessage() {
            return message;
        }
    }
}
