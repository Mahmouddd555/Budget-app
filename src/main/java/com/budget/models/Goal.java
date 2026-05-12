package com.budget.models;

import java.time.LocalDate;

public class Goal {
    private int id;
    private int userId;
    private String name;
    private double targetAmount;
    private double savedAmount;
    private LocalDate targetDate;
    private String status; // "ACTIVE", "ACHIEVED", "FAILED"

    public Goal() {
    }

    public Goal(int userId, String name, double targetAmount, LocalDate targetDate) {
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = 0.0;
        this.targetDate = targetDate;
        this.status = "ACTIVE";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getProgress() {
        if (targetAmount == 0)
            return 0;
        return (savedAmount / targetAmount) * 100;
    }

    public double getRemaining() {
        return targetAmount - savedAmount;
    }
}