package com.budget.models;

public class Budget {
    private int id;
    private int userId;
    private String category;
    private double allocatedAmount;
    private double spentAmount;
    private String period; // "DAILY", "WEEKLY", "MONTHLY"

    public Budget() {
    }

    public Budget(int userId, String category, double allocatedAmount, String period) {
        this.userId = userId;
        this.category = category;
        this.allocatedAmount = allocatedAmount;
        this.spentAmount = 0.0;
        this.period = period;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getRemainingAmount() {
        return allocatedAmount - spentAmount;
    }

    public double getPercentageUsed() {
        if (allocatedAmount == 0)
            return 0;
        return (spentAmount / allocatedAmount) * 100;
    }
}