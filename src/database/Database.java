package database;

import java.util.ArrayList;
import java.util.List;


import model.Budget;
import model.Goal;
import model.Notification;
import model.Transaction;
import model.User;

public class Database {
    public static List<User> users = new ArrayList<>();
    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Budget> budgets = new ArrayList<>();
    public static List<Goal> goals = new ArrayList<>();
    public static List<Notification> notifications = new ArrayList<>();
    

}