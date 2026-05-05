package controller;

import model.ExpenseTransaction;
import model.IncomeTransaction;
import service.TransactionService;

import java.time.LocalDate;

public class TransactionController {
    
    private TransactionService service = new TransactionService();

    public void addIncome(int id, double amount, LocalDate date, String description, String category) {
        service.createTransaction(new IncomeTransaction(id,amount,date,description,category));
    }

    public void addExpense(int id, double amount, LocalDate date, String description, String category) {
        service.createTransaction(new ExpenseTransaction(id,amount,date,description,category));
    }
}