package service;

import database.UserDAO;

public class AuthService {

    private UserDAO userDAO = new UserDAO();
    public boolean login(String user, String pass) {
        return userDAO.getAll().stream()
            .anyMatch(u -> u.getName().equals(user) && u.getPassword().equals(pass));
    }
}