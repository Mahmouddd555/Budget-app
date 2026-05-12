package com.budget.service;

import com.budget.models.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportService {

    public String generateDailyReport(List<Transaction> transactions, double totalIncome, double totalExpense,
            double netSavings) {
        StringBuilder report = new StringBuilder();
        report.append("========== DAILY FINANCIAL REPORT ==========\n");
        report.append("Date: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        report.append("===========================================\n\n");

        report.append("📊 SUMMARY:\n");
        report.append(String.format("💰 Total Income: $%.2f\n", totalIncome));
        report.append(String.format("💸 Total Expenses: $%.2f\n", totalExpense));
        report.append(String.format("💎 Net Savings: $%.2f\n", netSavings));
        report.append("\n📝 TRANSACTIONS:\n");

        for (Transaction t : transactions) {
            String symbol = t.getType().equals("INCOME") ? "+" : "-";
            report.append(String.format("  %s %s: %s$%.2f - %s\n",
                    t.getCategory(), t.getDescription(), symbol, t.getAmount(), t.getDate()));
        }

        return report.toString();
    }

    public String generateWeeklyReport(Map<String, Double> dailyIncome, Map<String, Double> dailyExpense,
            double weekTotalIncome, double weekTotalExpense, double weekNetSavings) {
        StringBuilder report = new StringBuilder();
        report.append("========== WEEKLY FINANCIAL REPORT ==========\n");
        report.append("Week of: ")
                .append(LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        report.append(" to ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        report.append("=============================================\n\n");

        report.append("📈 DAILY BREAKDOWN:\n");
        report.append("---------------------------------------------\n");
        report.append(String.format("%-12s %12s %12s %12s\n", "Day", "Income", "Expense", "Net"));
        report.append("---------------------------------------------\n");

        // Simplified for demo
        report.append("\n📊 WEEKLY SUMMARY:\n");
        report.append(String.format("💰 Total Income: $%.2f\n", weekTotalIncome));
        report.append(String.format("💸 Total Expenses: $%.2f\n", weekTotalExpense));
        report.append(String.format("💎 Net Savings: $%.2f\n", weekNetSavings));

        return report.toString();
    }

    public String generateMonthlyReport(Map<String, Double> categorySpending,
            double monthTotalIncome, double monthTotalExpense, double monthNetSavings) {
        StringBuilder report = new StringBuilder();
        report.append("========== MONTHLY FINANCIAL REPORT ==========\n");
        report.append("Month: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n");
        report.append("==============================================\n\n");

        report.append("📊 MONTHLY SUMMARY:\n");
        report.append(String.format("💰 Total Income: $%.2f\n", monthTotalIncome));
        report.append(String.format("💸 Total Expenses: $%.2f\n", monthTotalExpense));
        report.append(String.format("💎 Net Savings: $%.2f\n", monthNetSavings));
        report.append(String.format("📈 Savings Rate: %.1f%%\n", (monthNetSavings / monthTotalIncome) * 100));

        report.append("\n📂 SPENDING BY CATEGORY:\n");
        report.append("---------------------------------------------\n");
        for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
            report.append(String.format("  %-15s: $%.2f\n", entry.getKey(), entry.getValue()));
        }

        return report.toString();
    }
}