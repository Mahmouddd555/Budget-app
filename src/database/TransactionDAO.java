package database;

import model.Transaction;
import java.util.List;

public class TransactionDAO {
    public void save(Transaction transaction, int userId) {}
    public List<Transaction> findByUserId(int userId) {}
    public void delete(int transactionId) {}
}