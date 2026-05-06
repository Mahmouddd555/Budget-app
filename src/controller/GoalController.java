package controller;

import model.Goal;
import model.User;
import service.GoalService;
import java.util.List;

public class GoalController {

    private final GoalService goalService;

    public GoalController() {
        this.goalService = new GoalService();
    }

    public void addNewGoal(int id, String name, double target) {
        Goal goal = new Goal(id, name, target);
        goalService.createGoal(goal);
        System.out.println("Goal '" + name + "' created!");
    }

    public void depositToGoal(int id, double amount) {
        goalService.addProgress(id, amount);
        System.out.println("Amount added to your goal!");
    }


    public void checkGoalStatus(int id) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getCurrentAmount() >= goal.getTargetAmount()) {
            System.out.println("Congratulations! Goal " + goal.getName() + " achieved.");
        }
    }

    public List<Goal> getUserGoals(User user)
    {
        return goalService.getGoalsByUserId(user.getId());
    }
}