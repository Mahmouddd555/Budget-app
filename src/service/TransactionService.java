package service;

import model.Transaction;
import model.User;

import java.util.List;

public class TransactionService {

    public void addTransaction(User user, Transaction transaction) {}

    public void validateTransaction(Transaction transaction) {}

    public void updateBalance(User user, Transaction transaction) {}

    public List<Transaction> getUserTransactions(User user) {}

    public void deleteTransaction(User user, int transactionId) {}
}