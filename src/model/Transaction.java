package model;

import java.time.LocalDate;

public abstract class Transaction {

    protected int id;
    protected int userId;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected String category;

    private static int counter = 1;

    public Transaction(int userId, double amount, LocalDate date, String description, String category) {
        this.id = counter++;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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

    // مهم جدًا: كل نوع هيحدد نفسه
    public abstract String getType();
}