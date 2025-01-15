package com.example.spring_api.controller;

import com.example.spring_api.model.Transaction;
import com.example.spring_api.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private TransactionService service;

    @Test
    void testReturnAllTransactions() throws Exception {
        // Arrange
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setDescription("Transaction 1");

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setDescription("Transaction 2");

        when(service.getAllTransactions()).thenReturn(Arrays.asList(transaction1, transaction2));

        // Act & Assert
        mockMvc.perform(get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is("Transaction 1")))
                .andExpect(jsonPath("$[1].description", is("Transaction 2")));

        verify(service, times(1)).getAllTransactions();
    }

    @Test
    void testReturnTransactionById() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Test Transaction");

        when(service.getTransactionById(1L)).thenReturn(transaction);

        // Act & Assert
        mockMvc.perform(get("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Test Transaction")));

        verify(service, times(1)).getTransactionById(1L);
    }

    @Test
    void testReturn404WhenTransactionNotFound() throws Exception {
        // Arrange
        when(service.getTransactionById(1L)).thenThrow(new RuntimeException("Transaction not found"));

        // Act & Assert
        mockMvc.perform(get("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Transaction not found")));

        verify(service, times(1)).getTransactionById(1L);
    }


    @Test
    void testCreateTransaction() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("New Transaction");

        when(service.createTransaction(Mockito.any(Transaction.class))).thenReturn(transaction);

        // Act & Assert
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000.0,\"transactionDate\":\"2025-01-15\",\"scheduleDate\":\"2025-01-20\",\"description\":\"New Transaction\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("New Transaction")));

        verify(service, times(1)).createTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void testReturn400ForInvalidTransactionData() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Empty body
                .andExpect(status().isBadRequest());

        verify(service, times(0)).createTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Updated Transaction");

        when(service.updateTransaction(eq(1L), Mockito.any(Transaction.class))).thenReturn(transaction);

        // Act & Assert
        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1500.0,\"transactionDate\":\"2025-01-15\",\"scheduleDate\":\"2025-01-25\",\"description\":\"Updated Transaction\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Updated Transaction")));

        verify(service, times(1)).updateTransaction(eq(1L), Mockito.any(Transaction.class));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/transactions/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteTransaction(1L);
    }
}
