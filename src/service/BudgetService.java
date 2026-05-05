package service;

import model.Budget;
import database.BudgetDAO;

public class BudgetService {

    private BudgetDAO budgetDAO= new BudgetDAO();

    public void addBudget(Budget budget) {
        if (budget.getAmount() <= 0) {
            throw new IllegalArgumentException("Budget amount must be greater than zero.");
        }
        budgetDAO.addBudget(budget);
    }

    public java.util.List<Budget> getBudgets() {
        return budgetDAO.getAllBudgets();
    }

    public void updateBudget(int id, double newLimit) {
        if (newLimit <= 0) {
            throw new IllegalArgumentException("Budget limit must be greater than zero.");
        }
        java.util.List<Budget> budgets = budgetDAO.getAllBudgets();
        for (Budget budget : budgets) {
            if (budget.getId() == id) {
                budget.setAmount(newLimit);
                budgetDAO.updateBudget(budget);
                return;
            }
        }
        throw new IllegalArgumentException("Budget with ID " + id + " not found.");
    }
}