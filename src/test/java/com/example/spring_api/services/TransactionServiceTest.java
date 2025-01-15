package com.example.spring_api.services;

import org.junit.jupiter.api.Test;
import com.example.spring_api.model.Transaction;
import com.example.spring_api.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private final TransactionRepository repository = mock(TransactionRepository.class);
    private final TransactionService service = new TransactionService(repository);

    @Test
    void testCreateTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setDescription("Test Transaction");

        when(repository.save(transaction)).thenReturn(transaction);

        // Act
        Transaction createdTransaction = service.createTransaction(transaction);

        // Assert
        assertThat(createdTransaction.getDescription()).isEqualTo("Test Transaction");
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testGetTransactionById() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Test Transaction");

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));

        // Act
        Transaction retrievedTransaction = service.getTransactionById(1L);

        // Assert
        assertThat(retrievedTransaction.getDescription()).isEqualTo("Test Transaction");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetAllTransactions() {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setDescription("Transaction 1");

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setDescription("Transaction 2");

        when(repository.findAll()).thenReturn(List.of(transaction1, transaction2));

        // Act
        List<Transaction> transactions = service.getAllTransactions();

        // Assert
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getDescription()).isEqualTo("Transaction 1");
        assertThat(transactions.get(1).getDescription()).isEqualTo("Transaction 2");
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateTransaction() {
        // Arrange
        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(1L);
        existingTransaction.setDescription("Old Description");

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDescription("New Description");

        when(repository.findById(1L)).thenReturn(Optional.of(existingTransaction));
        when(repository.save(existingTransaction)).thenReturn(existingTransaction);

        // Act
        Transaction result = service.updateTransaction(1L, updatedTransaction);

        // Assert
        assertThat(result.getDescription()).isEqualTo("New Description");
        verify(repository, times(1)).save(existingTransaction);
    }

    @Test
    void testDeleteTransaction() {
        // Act
        service.deleteTransaction(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }
}
