package service;

import model.Budget;
import model.Transaction;
import model.User;

import java.util.List;

public class BudgetService {

    public void createBudget(User user, Budget budget) {}

    public void updateBudget(Budget budget, Transaction transaction) {}

    public double calculateRemaining(Budget budget) {}

    public boolean checkBudgetExceeded(Budget budget) {}

    public List<Budget> getUserBudgets(User user) {}
}