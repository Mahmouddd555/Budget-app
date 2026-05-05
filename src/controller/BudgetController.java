package controller;

import model.Budget;
import service.BudgetService;
import java.util.List;

public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController() {
        this.budgetService = new BudgetService();
    }

    // إضافة ميزانية جديدة
    public void addBudget(int id, double limit, String month) {
        Budget budget = new Budget(id, limit, month);
        budgetService.addBudget(budget);
        System.out.println("Budget added successfully for: " + month);
    }

    public List<Budget> getAllBudgets() {
        return budgetService.getBudgets();
    }

    public void updateBudget(int id, double newLimit) {
        budgetService.updateBudget(id, newLimit);
    }
}