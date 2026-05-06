package controller;

import model.User;
import service.AuthService;

public class AuthController {

    private final AuthService authService = new AuthService();

    public User handleLogin(String email, String password)
    {
        return authService.login(email, password);
    }
    public User handleRegister(String name, String email, String password, String currency, String language)
    {
        return authService.register(name, email, password, currency, language);
    }
}