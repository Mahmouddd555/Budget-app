package controller;

import model.Transaction;
import model.User;
import java.util.List;

public class TransactionController {
    public void addTransaction(User user, Transaction transaction) {}
    public List<Transaction> getUserTransactions(User user) {}
    public void deleteTransaction(User user, int transactionId) {}
}