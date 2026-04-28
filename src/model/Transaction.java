package model;

import java.time.LocalDate;

public abstract class Transaction {

    protected int id;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected Category category;

    public Transaction(int id, double amount, LocalDate date, String description, Category category) {}

    public double getAmount() {}

    public Category getCategory() {}

    public abstract String getType();
}