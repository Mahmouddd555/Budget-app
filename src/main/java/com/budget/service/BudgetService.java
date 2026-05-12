package com.budget.service;

import com.budget.database.BudgetDAO;
import com.budget.database.TransactionDAO;
import com.budget.models.Budget;
import com.budget.models.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetService {
    private BudgetDAO budgetDAO;
    private TransactionDAO transactionDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public boolean createBudget(int userId, String category, double allocatedAmount, String period) {
        Budget budget = new Budget(userId, category, allocatedAmount, period);
        return budgetDAO.createBudget(budget);
    }

    public List<Budget> getUserBudgets(int userId) {
        return budgetDAO.getBudgetsByUserId(userId);
    }

    public void updateBudgetsSpending(int userId, String period) {
        List<Budget> budgets = budgetDAO.getBudgetsByUserId(userId);
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toUpperCase()) {
            case "DAILY":
                startDate = endDate;
                break;
            case "WEEKLY":
                startDate = endDate.minusWeeks(1);
                break;
            default:
                startDate = endDate.minusMonths(1);
        }

        List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(userId, startDate, endDate);

        for (Budget budget : budgets) {
            double spent = transactions.stream()
                    .filter(t -> t.getType().equals("EXPENSE") && t.getCategory().equals(budget.getCategory()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            budgetDAO.updateSpentAmount(budget.getId(), spent);
        }
    }

    public List<Budget> getBudgetsNearLimit(int userId, double threshold) {
        List<Budget> budgets = budgetDAO.getBudgetsByUserId(userId);
        return budgets.stream()
                .filter(b -> b.getPercentageUsed() >= threshold)
                .collect(Collectors.toList());
    }

    public boolean deleteBudget(int budgetId) {
        return budgetDAO.deleteBudget(budgetId);
    }
}