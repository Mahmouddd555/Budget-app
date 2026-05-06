package service;

import model.Budget;
import model.Transaction;
import database.BudgetDAO;
import database.TransactionDAO;
import java.util.List;

public class BudgetService {

    private final BudgetDAO budgetDAO= new BudgetDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public void addBudget(Budget budget) {
        if (budget.getAmount() <= 0) {
            throw new IllegalArgumentException("Budget amount must be greater than zero.");
        }
        budgetDAO.addBudget(budget);
    }


    public void updateBudget(int id, double newLimit) {
        if (newLimit <= 0) {
            throw new IllegalArgumentException("Budget limit must be greater than zero.");
        }
        java.util.List<Budget> budgets =getBudgetsByUserId(id);
        for (Budget budget : budgets) {
            if (budget.getId() == id) {
                budget.setAmount(newLimit);
                budgetDAO.updateBudget(budget);
                return;
            }
        }
        throw new IllegalArgumentException("Budget with ID " + id + " not found.");
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        return budgetDAO.getBudgetsByUserId(userId);
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionDAO.findByUserId(userId);
    }
}