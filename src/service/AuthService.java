package service;

import database.UserDAO;
import model.User;

public class AuthService
{
    private final UserDAO userDAO = new UserDAO();
    public User login(String email, String password)
    {
        User user = userDAO.findByEmail(email);
        if (user != null && user.getPassword().equals(password))
        {
            return user;
        }
        return null;
    }

    public User register(String name, String email, String password, String currency, String language)
    {
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null)
        {
            return null;
        }
        int newId = userDAO.getAll().size() + 1;
        User newUser = new User(newId, name, email, password);
        userDAO.save(newUser);

        return newUser;
    }
}