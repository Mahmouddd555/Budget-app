package service;

import model.Transaction;
import model.User;
import database.TransactionDAO;

import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;

    public void addTransaction(User user, Transaction transaction) {}

    public void validateTransaction(Transaction transaction) {}

    public void updateBalance(User user, Transaction transaction) {}

    public List<Transaction> getUserTransactions(int userId) {
        return transactionDAO.findByUserId(userId);
    }

    public void deleteTransaction(User user, int transactionId) {}
}