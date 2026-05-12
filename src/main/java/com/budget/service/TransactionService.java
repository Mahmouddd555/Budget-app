package com.budget.service;

import com.budget.database.TransactionDAO;
import com.budget.database.UserDAO;
import com.budget.models.Transaction;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionService {
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.userDAO = new UserDAO();
    }

    public boolean addTransaction(int userId, String type, double amount, String category, String description,
            LocalDate date) {
        Transaction transaction = new Transaction(userId, type, amount, category, description, date);
        boolean success = transactionDAO.addTransaction(transaction);

        if (success) {
            updateUserBalance(userId);
        }

        return success;
    }

    public List<Transaction> getUserTransactions(int userId) {
        return transactionDAO.getTransactionsByUserId(userId);
    }

    public List<Transaction> getTransactionsByPeriod(int userId, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "today":
                startDate = endDate;
                break;
            case "weekly":
                startDate = endDate.minusWeeks(1);
                break;
            case "monthly":
                startDate = endDate.minusMonths(1);
                break;
            default:
                startDate = endDate.minusMonths(1);
        }

        return transactionDAO.getTransactionsByDateRange(userId, startDate, endDate);
    }

    public Map<String, Double> getSpendingByCategory(int userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(userId, startDate, endDate);
        Map<String, Double> spendingByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getType().equals("EXPENSE")) {
                spendingByCategory.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }

        return spendingByCategory;
    }

    public Map<String, Double> getIncomeByCategory(int userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(userId, startDate, endDate);
        Map<String, Double> incomeByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getType().equals("INCOME")) {
                incomeByCategory.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }

        return incomeByCategory;
    }

    public double getTotalIncome(int userId, LocalDate startDate, LocalDate endDate) {
        return transactionDAO.getTotalByTypeAndDateRange(userId, "INCOME", startDate, endDate);
    }

    public double getTotalExpense(int userId, LocalDate startDate, LocalDate endDate) {
        return transactionDAO.getTotalByTypeAndDateRange(userId, "EXPENSE", startDate, endDate);
    }

    public double getNetSavings(int userId, LocalDate startDate, LocalDate endDate) {
        double income = getTotalIncome(userId, startDate, endDate);
        double expense = getTotalExpense(userId, startDate, endDate);
        return income - expense;
    }

    private void updateUserBalance(int userId) {
        List<Transaction> allTransactions = transactionDAO.getTransactionsByUserId(userId);
        double balance = 0;

        for (Transaction t : allTransactions) {
            if (t.getType().equals("INCOME")) {
                balance += t.getAmount();
            } else {
                balance -= t.getAmount();
            }
        }

        userDAO.updateUserBalance(userId, balance);
    }

    public boolean deleteTransaction(int transactionId, int userId) {
        boolean success = transactionDAO.deleteTransaction(transactionId);
        if (success) {
            updateUserBalance(userId);
        }
        return success;
    }
}