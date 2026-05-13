package com.budget.controller;

import com.budget.models.Goal;
import com.budget.service.GoalService;

import java.time.LocalDate;
import java.util.List;

/**
 * Goal use-cases (delegates to {@link GoalService}).
 */
public class GoalController {

    private final GoalService goalService;

    public GoalController() {
        this(new GoalService());
    }

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    public List<Goal> loadGoals(int userId) {
        return goalService.getUserGoals(userId);
    }

    public boolean createGoal(int userId, String name, double targetAmount, LocalDate targetDate) {
        return goalService.createGoal(userId, name, targetAmount, targetDate);
    }

    public boolean updateSavedAmount(int goalId, double newSavedAmount) {
        return goalService.updateSavedAmount(goalId, newSavedAmount);
    }

    public boolean updateGoalStatus(int goalId, String status) {
        return goalService.updateGoalStatus(goalId, status);
    }

    public boolean deleteGoal(int goalId) {
        return goalService.deleteGoal(goalId);
    }
}
