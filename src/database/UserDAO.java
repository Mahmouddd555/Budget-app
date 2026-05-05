package database;

import java.util.List;

import model.User;

public class UserDAO {

    public void save(User user) {
        Database.users.add(user);
    }

    public User findByEmail(String email) {
        return Database.users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User findById(int id) {
        return Database.users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public void update(User user) {
        User existingUser = findById(user.getId());
        if (existingUser != null) {
            Database.users.remove(existingUser);
            Database.users.add(user);
        }
    }

    public void delete(int id) {
        User user = findById(id);
        if (user != null) {
            Database.users.remove(user);
        }
    }

    public List<User> getAll() {
        return Database.users;
    }
}