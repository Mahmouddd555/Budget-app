package model;

public class Goal {

    private int id;
    private String name;
    private double targetAmount;
    private double currentAmount;


    public Goal(int id, String name, double targetAmount, double currentAmount) {
        this.id =id;
        this.name=name;
        this.targetAmount=targetAmount;
        this.currentAmount=currentAmount;
    }
    public Goal(int userId, String name, double targetAmount) {
        this.id = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
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
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getTargetAmount() { return targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
}