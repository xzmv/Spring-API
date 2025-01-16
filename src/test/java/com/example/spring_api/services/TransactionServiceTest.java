package com.example.spring_api.services;

import org.junit.jupiter.api.Test;
import com.example.spring_api.model.Transaction;
import com.example.spring_api.repository.TransactionRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private final TransactionRepository repository = mock(TransactionRepository.class);
    private final TransactionService service = new TransactionService(repository);

    @Test
    void testCalculateFeeForSameDayTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now());

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        Double expectedFee = (1000.0 * 0.03) + 3.0;
        assertThat(createdTransaction.getTransferFee()).isEqualTo(expectedFee);
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testCalculateFeeFor1To10DaysTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(1500.0);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now().plusDays(5));

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        Double expectedFee = 1500.0 * 0.09;
        assertThat(createdTransaction.getTransferFee()).isEqualTo(expectedFee);
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testCalculateFeeFor11To20DaysTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(2500.0);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now().plusDays(15));

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        Double expectedFee = 2500.0 * 0.082;
        assertThat(createdTransaction.getTransferFee()).isEqualTo(expectedFee);
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testCalculateFeeFor21To30DaysTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(2500.0);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now().plusDays(25));

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        Double expectedFee = 2500.0 * 0.069;
        assertThat(createdTransaction.getTransferFee()).isEqualTo(expectedFee);
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testCalculateFeeForMoreThan40DaysTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(3000.0);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now().plusDays(45));

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        Double expectedFee = 3000.0 * 0.017;
        assertThat(createdTransaction.getTransferFee()).isEqualTo(expectedFee);
        verify(repository, times(1)).save(transaction);
    }
}
