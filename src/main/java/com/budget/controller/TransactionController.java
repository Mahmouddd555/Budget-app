package com.budget.controller;

import com.budget.database.DatabaseConnection;
import com.budget.models.Transaction;
import com.budget.service.TransactionService;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Coordinates transaction listing, filtering, persistence, and export (no
 * JavaFX).
 */
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController() {
        this(new TransactionService());
    }

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // get all transactions for a user (no filtering)
    public List<Transaction> loadAllTransactions(int userId) {
        return new ArrayList<>(transactionService.getUserTransactions(userId));
    }

    // add a new transaction by ID for a user
    public boolean addTransaction(int userId, String type, double amount, String category, String description,
            LocalDate date) {
        return transactionService.addTransaction(userId, type, amount, category, description, date);
    }

    // delete a transaction by ID for a user
    public boolean deleteTransaction(int transactionId, int userId) {
        return transactionService.deleteTransaction(transactionId, userId);
    }

    // resolve the start date for a given period label
    public LocalDate resolvePeriodStart(String periodLabel, LocalDate now) {
        if (periodLabel == null) {
            return now.withDayOfMonth(1);
        }
        return switch (periodLabel) {
            case "Today" -> now;
            case "This Week" -> now.minusWeeks(1);
            case "This Month" -> now.withDayOfMonth(1);
            case "This Year" -> now.withDayOfYear(1);
            default -> LocalDate.of(2000, 1, 1);
        };
    }

    // filter transactions by a start date (inclusive)
    public List<Transaction> filterByPeriod(List<Transaction> all, LocalDate startInclusive) {
        return all.stream().filter(t -> !t.getDate().isBefore(startInclusive)).toList();
    }

    // apply type and search filters to a list of transactions
    public List<Transaction> applySearchAndType(List<Transaction> source, String typeFilter, String searchRaw) {
        String search = searchRaw == null ? "" : searchRaw.toLowerCase(Locale.ROOT);
        String type = typeFilter == null ? "All" : typeFilter;
        return source.stream()
                .filter(t -> "All".equals(type) || t.getType().equals(type.toUpperCase(Locale.ROOT)))
                .filter(t -> search.isEmpty()
                        || (t.getCategory() != null && t.getCategory().toLowerCase(Locale.ROOT).contains(search))
                        || (t.getDescription() != null && t.getDescription().toLowerCase(Locale.ROOT).contains(search)))
                .toList();
    }

    // calculate summary totals for a list of transactions
    public record Summary(double totalIncome, double totalExpense, double net) {
    }

    public Summary summarize(List<Transaction> transactions) {
        double income = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        return new Summary(income, expense, income - expense);
    }

    // export a list of transactions to a CSV file
    public void exportToCsv(List<Transaction> rows, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("ID,Type,Amount,Category,Description,Date\n");
            for (Transaction t : rows) {
                writer.append(String.valueOf(t.getId())).append(",");
                writer.append(t.getType()).append(",");
                writer.append(String.valueOf(t.getAmount())).append(",");
                writer.append(escapeCsv(t.getCategory())).append(",");
                writer.append(escapeCsv(t.getDescription())).append(",");
                writer.append(t.getDate().toString()).append("\n");
            }
        }
    }

    // helper to escape CSV fields
    private static String escapeCsv(String s) {
        if (s == null) {
            return "";
        }
        String x = s.replace("\"", "\"\"");
        if (x.contains(",") || x.contains("\"") || x.contains("\n")) {
            return "\"" + x + "\"";
        }
        return x;
    }
}
