package com.budget.controller;

import com.budget.models.Budget;
import com.budget.service.BudgetService;

import java.util.List;

/**
 * Budget use-cases (delegates to {@link BudgetService}).
 */
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController() {
        this(new BudgetService());
    }

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public List<Budget> loadBudgetsForUser(int userId) {
        return budgetService.getUserBudgets(userId);
    }

    public void refreshSpending(int userId, String period) {
        budgetService.updateBudgetsSpending(userId, period);
    }

    public boolean createBudget(int userId, String category, double amount, String period) {
        return budgetService.createBudget(userId, category, amount, period);
    }

    public void deleteBudget(int budgetId) {
        budgetService.deleteBudget(budgetId);
    }
}
