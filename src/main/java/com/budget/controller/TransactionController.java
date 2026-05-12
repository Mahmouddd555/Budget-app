// package main.java.com.budget.controller;

// import java.time.LocalDate;
// import java.util.List;

// import main.java.com.budget.models.ExpenseTransaction;
// import main.java.com.budget.models.IncomeTransaction;
// import main.java.com.budget.models.Transaction;
// import main.java.com.budget.models.User;
// import main.java.com.budget.service.TransactionService;

// public class TransactionController {

//     private TransactionService service = new TransactionService();

//     public void addIncome(int id, double amount, LocalDate date, String description, String category) {
//         service.createTransaction(new IncomeTransaction(id, amount, date, description, category));
//     }

//     public void addExpense(int id, double amount, LocalDate date, String description, String category) {
//         service.createTransaction(new ExpenseTransaction(id, amount, date, description, category));
//     }

//     public List<Transaction> getUserTransactions(User user) {
//         return service.getTransactionsByUserId(user.getId());
//     }
// }