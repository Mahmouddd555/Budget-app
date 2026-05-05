package model;

import java.time.LocalDate;

public abstract class Transaction {

    protected int userId;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected String category;

    public Transaction(int userId, double amount, LocalDate date, String description, String category) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
    public int getId() {
        return userId;
    }

    public String getType() {
        return "Transaction";
    }
}