package com.example.spring_api.repository;

import com.example.spring_api.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testGetterAndSetters() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(1000.0);
        transaction.setDescription("Test transaction");
        transaction.setTransactionDate(LocalDate.now());
        transaction.setScheduleDate(LocalDate.now().plusDays(5));

        // Act
        Transaction savedTransaction = transactionRepository.save(transaction);
        Optional<Transaction> retrievedTransaction = transactionRepository.findById(savedTransaction.getId());

        // Assert
        assertThat(retrievedTransaction).isPresent();
        assertThat(retrievedTransaction.get().getDescription()).isEqualTo("Test transaction");
    }

}