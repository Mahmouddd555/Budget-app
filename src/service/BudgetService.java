package service;

import model.Budget;
// import model.Transaction;
// import model.User;
import database.BudgetDAO;

import java.util.List;


public class BudgetService {
    private BudgetDAO budgetDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
    }

    public void addBudget(Budget budget) {
        if (budget.getAmount() <= 0) {
            throw new IllegalArgumentException("Budget must be positive");
        }
        budgetDAO.insertBudget(budget);
    }

    public List<Budget> getUserBudgets(int userId) {
        return budgetDAO.getBudgetsByUserId(userId);
    }

    public void updateBudget(Budget budget) {
        if (budget.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        budgetDAO.updateBudget(budget);
    }

    public void deleteBudget(int id) {
        budgetDAO.deleteBudget(id);
    }
}