package service;

import model.Goal;
import database.GoalDAO;

import java.util.List;

public class GoalService {
    private GoalDAO goalDAO;

    public GoalService() {
        this.goalDAO = new GoalDAO();
    }

    public void createGoal( Goal goal) {
        if (goal.getTargetAmount() <= 0) {
            throw new IllegalArgumentException("Target must be positive");
        }

        goalDAO.save(goal);
    }

    public void updateProgress(Goal goal, double amount) {}

    public double calculateProgress(Goal goal) {
        return goal.getProgress();
    }

    public List<Goal> getUserGoals(int userId) {
        return goalDAO.findByUserId(userId);
    }
}