// package main.java.com.budget.controller;

// import java.util.List;

// import main.java.com.budget.models.Budget;
// import main.java.com.budget.models.Transaction;
// import main.java.com.budget.models.User;
// import main.java.com.budget.service.BudgetService;

// public class BudgetController {
//     private final BudgetService budgetService;

//     public BudgetController() {
//         this.budgetService = new BudgetService();
//     }

//     // إضافة ميزانية جديدة
//     public void addBudget(int id, double limit, String month) {
//         Budget budget = new Budget(id, limit, month);
//         budgetService.addBudget(budget);
//         System.out.println("Budget added successfully for: " + month);
//     }

//     public List<Budget> getAllBudgets() {
//         return budgetService.getBudgets();
//     }

//     public void updateBudget(int id, double newLimit) {
//         budgetService.updateBudget(id, newLimit);
//     }

//     public List<Budget> getUserBudgets(User user) {
//         return budgetService.getBudgetsByUserId(user.getId());
//     }

//     public List<Transaction> getTransactionsByUserId(int userId) {
//         return budgetService.getTransactionsByUserId(userId);
//     }
// }