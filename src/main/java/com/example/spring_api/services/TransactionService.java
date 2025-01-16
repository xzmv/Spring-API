package com.example.spring_api.services;

import com.example.spring_api.model.Transaction;
import com.example.spring_api.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setTransferFee(calculateFee(transaction));
        return repository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existingTransaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setDescription(updatedTransaction.getDescription());
        existingTransaction.setTransactionDate(updatedTransaction.getTransactionDate());
        existingTransaction.setScheduleDate(updatedTransaction.getScheduleDate());
        existingTransaction.setTransferFee(calculateFee(updatedTransaction));

        return repository.save(existingTransaction);
    }

    public void deleteTransaction(Long id) {
        repository.deleteById(id);
    }

    private Double calculateFee(Transaction transaction) {
        Double amount = transaction.getAmount();
        LocalDate transactionDate = transaction.getTransactionDate();
        LocalDate scheduleDate = transaction.getScheduleDate();

        long daysBetween = ChronoUnit.DAYS.between(transactionDate, scheduleDate);

        if (amount <= 1000.0 && daysBetween == 0) {
            return (amount * 0.03) + 3.0;
        } else if (amount <= 2000.0 && daysBetween >= 1 && daysBetween <= 10) {
            return amount * 0.09;
        } else if (amount > 2000.0) {
            if (daysBetween >= 11 && daysBetween <= 20) {
                return amount * 0.082;
            } else if (daysBetween >= 21 && daysBetween <= 30) {
                return amount * 0.069;
            } else if (daysBetween >= 31 && daysBetween <= 40) {
                return amount * 0.047;
            } else if (daysBetween > 40) {
                return amount * 0.017;
            }
        }
        throw new IllegalArgumentException("Invalid scheduling date or amount");
    }
}
