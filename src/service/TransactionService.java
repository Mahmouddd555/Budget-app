package service;

import model.Transaction;
import database.TransactionDAO;


public class TransactionService {

    private TransactionDAO dao = new TransactionDAO();
    public void createTransaction(Transaction t) { dao.save(t); }

}