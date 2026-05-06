package service;

import model.Goal;

import java.util.List;

import database.GoalDAO;

public class GoalService {

    private GoalDAO dao = new GoalDAO();

    public void createGoal(Goal goal) {
        dao.save(goal);
    }

    public void addProgress(int id, double amount) {
        Goal goal = dao.findById(id);
        if (goal != null) {
            goal.setCurrentAmount(goal.getCurrentAmount() + amount);
            dao.update(goal);
        }
    }

    public Goal getGoalById(int Id) {
        return dao.findById(Id);
    }


    public List<Goal> getGoalsByUserId(int userId) {
        return dao.findByUserId(userId);
    }
}