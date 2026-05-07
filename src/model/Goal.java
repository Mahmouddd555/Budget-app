package model;

public class Goal {

    private int id;
    private int userId;
    private String name;
    private double targetAmount;
    private double currentAmount = 0;

    public Goal(int id, int userId, String name, double targetAmount) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void updateProgress(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (currentAmount + amount > targetAmount) {
            throw new IllegalArgumentException("Cannot exceed target amount");
        }

        currentAmount += amount;
    }

    public double getProgress() {
        if (targetAmount == 0)
            return 0;
        return (currentAmount / targetAmount) * 100;
    }
}