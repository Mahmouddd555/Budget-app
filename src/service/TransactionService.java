package service;

import model.Transaction;
import database.TransactionDAO;
import java.util.List;


public class TransactionService {

    private TransactionDAO dao = new TransactionDAO();
    public void createTransaction(Transaction t) { dao.save(t); }

    public List<Transaction> getTransactionsByUserId(int userId)
    {
        return dao.findByUserId(userId);
    }

}