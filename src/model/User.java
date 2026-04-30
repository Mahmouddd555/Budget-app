package model;

import java.util.List;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;

    private List<Transaction> transactions;
    private List<Budget> budgets;
    private List<Goal> goals;

    public User(int id, String name, String email, String password) {}

    public void addTransaction(Transaction transaction) {}

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addBudget(Budget budget) {}

    public void addGoal(Goal goal) {}

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}