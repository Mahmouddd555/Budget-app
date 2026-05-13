package com.budget.controller;

import com.budget.service.AuthService;
import com.budget.ui.SettingsManager;

/**
 * Handles registration and initial regional preferences (currency / language).
 */
public class RegisterController {

    private final AuthService authService;

    public RegisterController() {
        this(new AuthService());
    }

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    public RegisterResult register(String username, String email, String password, String confirmPassword) {
        if (username == null || username.isBlank() || email == null || email.isBlank()
                || password == null || password.isEmpty()) {
            return RegisterResult.failure("Please fill in all fields");
        }
        if (!password.equals(confirmPassword)) {
            return RegisterResult.failure("Passwords do not match");
        }
        if (password.length() < 4) {
            return RegisterResult.failure("Password must be at least 4 characters");
        }
        if (authService.register(username.trim(), email.trim(), password)) {
            return RegisterResult.success();
        }
        return RegisterResult.failure("Username or email already exists");
    }

    /** Persists currency symbol and language code via {@link SettingsManager}. */
    public void applyRegionalPreferences(String currencyDisplayLabel, String languageDisplayLabel) {
        String symbol = resolveCurrencySymbol(currencyDisplayLabel);
        SettingsManager.setCurrency(symbol);
        String lang = (languageDisplayLabel != null && languageDisplayLabel.contains("AR")) ? "AR" : "EN";
        SettingsManager.setLanguage(lang);
    }

    private static String resolveCurrencySymbol(String selected) {
        if (selected == null) {
            return "$";
        }
        if (selected.contains("€")) {
            return "€";
        }
        if (selected.contains("£")) {
            return "£";
        }
        if (selected.contains("¥")) {
            return "¥";
        }
        return "$";
    }

    public static final class RegisterResult {
        private final boolean success;
        private final String message;

        private RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static RegisterResult success() {
            return new RegisterResult(true, null);
        }

        public static RegisterResult failure(String message) {
            return new RegisterResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
