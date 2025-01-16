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

        // Validate input
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (transactionDate == null || scheduleDate == null) {
            throw new IllegalArgumentException("Transaction date and schedule date must not be null.");
        }

        long daysBetween = ChronoUnit.DAYS.between(transactionDate, scheduleDate);

        // Fee A: Same-day transactions, amount <= 1000
        if (amount <= 1000.0 && daysBetween == 0) {
            return (amount * 0.03) + 3.0;
        }

        // Fee B: Transactions 1–10 days, amount <= 2000
        if (amount <= 2000.0 && daysBetween >= 1 && daysBetween <= 10) {
            return amount * 0.09;
        }

        // Fee C: Transactions > 2000
        if (amount > 2000.0) {
            if (daysBetween >= 11 && daysBetween <= 20) {
                return amount * 0.082;
            } else if (daysBetween >= 21 && daysBetween <= 30) {
                return amount * 0.069;
            } else if (daysBetween >= 31 && daysBetween <= 40) {
                return amount * 0.047;
            } else if (daysBetween > 40) {
                return amount * 0.017;
            } else {
                // Explicitly handle cases where daysBetween < 11
                throw new IllegalArgumentException("For transactions above 2000, the scheduling date must be at least 11 days after the transaction date.");
            }
        }
        throw new IllegalArgumentException("Invalid scheduling date or amount");
    }
}
