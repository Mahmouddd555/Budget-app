package controller;

import model.Goal;
import model.User;
import service.GoalService;

import java.util.List;

public class GoalController {

    private GoalService goalService;

    public GoalController() {
        this.goalService = new GoalService();
    }

    public void createGoal(int userId, String name, double target) {
        try {
            Goal goal = new Goal(userId, name, target);
            goalService.createGoal(goal);
            System.out.println("Goal created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateGoalProgress(User user, int goalId, double amount) {}
    public List<Goal> getUserGoals(int userId) {
        return goalService.getUserGoals(userId);
    }
}