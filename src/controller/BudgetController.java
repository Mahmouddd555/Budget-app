package controller;

import model.Budget;
// import model.User;
import service.BudgetService;

import java.util.List;

public class BudgetController {
    private BudgetService budgetService;

    public BudgetController() {
        this.budgetService = new BudgetService();
    }

    public void createBudget(int userId, double amount, String category) {
        try {
            Budget budget = new Budget(userId, amount, category);
            budgetService.addBudget(budget);
            System.out.println("Budget added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Budget> getBudgets(int userId) {
        return budgetService.getUserBudgets(userId);
    }

    public void updateBudget(int id, double amount, String category) {
        try {
            Budget budget = new Budget(id, 0, amount, category);
            budgetService.updateBudget(budget);
            System.out.println("Updated successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteBudget(int id) {
        budgetService.deleteBudget(id);
        System.out.println("Deleted successfully!");
    }
}