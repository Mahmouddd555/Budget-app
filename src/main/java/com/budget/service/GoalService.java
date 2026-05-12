package com.budget.service;

import com.budget.database.GoalDAO;
import com.budget.models.Goal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GoalService {
    private GoalDAO goalDAO;

    public GoalService() {
        this.goalDAO = new GoalDAO();
    }

    public boolean createGoal(int userId, String name, double targetAmount, LocalDate targetDate) {
        Goal goal = new Goal(userId, name, targetAmount, targetDate);
        return goalDAO.createGoal(goal);
    }

    public List<Goal> getUserGoals(int userId) {
        return goalDAO.getGoalsByUserId(userId);
    }

    public boolean updateSavedAmount(int goalId, double newSavedAmount) {
        return goalDAO.updateSavedAmount(goalId, newSavedAmount);
    }

    public boolean updateGoalStatus(int goalId, String status) {
        return goalDAO.updateGoalStatus(goalId, status);
    }

    public List<Goal> getActiveGoals(int userId) {
        return goalDAO.getGoalsByUserId(userId).stream()
                .filter(g -> g.getStatus().equals("ACTIVE"))
                .collect(Collectors.toList());
    }

    public List<Goal> getAchievedGoals(int userId) {
        return goalDAO.getGoalsByUserId(userId).stream()
                .filter(g -> g.getStatus().equals("ACHIEVED"))
                .collect(Collectors.toList());
    }

    public boolean deleteGoal(int goalId) {
        return goalDAO.deleteGoal(goalId);
    }
}