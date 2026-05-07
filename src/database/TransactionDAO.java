package database;

import model.Transaction;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDAO {

    public void save(Transaction transaction) {
        Database.transactions.add(transaction);
    }

    public List<Transaction> findByUserId(int userId) {
        return Database.transactions.stream()
                .filter(t -> t.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void delete(int transactionId) {
        Transaction transaction = Database.transactions.stream()
                .filter(t -> t.getId() == transactionId)
                .findFirst()
                .orElse(null);

        if (transaction != null) {
            Database.transactions.remove(transaction);
        }
    }
}