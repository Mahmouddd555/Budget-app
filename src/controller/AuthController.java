package controller;

import service.AuthService;

public class AuthController {

    private AuthService authService = new AuthService();
    
    public void handleLogin(String u, String p) {
        if(authService.login(u, p)) System.out.println("Success");
    }
}