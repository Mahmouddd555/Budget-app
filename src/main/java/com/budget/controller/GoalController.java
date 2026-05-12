// package main.java.com.budget.controller;

// import java.util.List;

// import main.java.com.budget.models.Goal;
// import main.java.com.budget.models.User;
// import main.java.com.budget.service.GoalService;

// public class GoalController {

//     private final GoalService goalService;

//     public GoalController() {
//         this.goalService = new GoalService();
//     }

//     public void addNewGoal(int goalId, int userId, String name, double target) {
//         Goal goal = new Goal(goalId, userId, name, target);
//         goalService.createGoal(goal);
//     }

//     public void depositToGoal(int id, double amount) {
//         goalService.addProgress(id, amount);
//     }

//     public List<Goal> getUserGoals(User user) {
//         return goalService.getGoalsByUserId(user.getId());
//     }

//     public List<Goal> getMyGoals() {
//         return goalService.getAllGoals();
//     }
// }