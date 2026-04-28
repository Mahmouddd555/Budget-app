package model;

public class Budget {

    private int id;
    private Category category;
    private double limit;
    private double spent;

    public Budget(int id, Category category, double limit) {}

    public void addExpense(double amount) {}

    public boolean isExceeded() {}

    public double getRemaining() {}
}