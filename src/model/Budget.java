package model;

public class Budget {
    private int userId;
    private double amount;
    private String category;


    public Budget(int userId, double amount, String category) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
    }

    public int getId() { return userId; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
}