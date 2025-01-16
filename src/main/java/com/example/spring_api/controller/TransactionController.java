package com.example.spring_api.controller;

import com.example.spring_api.model.Transaction;
import com.example.spring_api.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new transaction", description = "Adds a new transaction and calculates the transfer fee based on scheduling rules.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "id": 1,
                                          "amount": 1500,
                                          "transactionDate": "2025-01-15",
                                          "scheduleDate": "2025-01-20",
                                          "description": "Payment for services",
                                          "transferFee": 135.0
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data",
                    content = @Content(mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "error": "Transaction date must be on or before the schedule date."
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "409", description = "Transaction with ID already exists",
                    content = @Content(mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "error": "Transaction with this ID already exists."
                                        }
                                        """
                            )))
    })
    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody Transaction transaction) {
        if (transaction.getId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Transaction with this ID already exists.");
        }
        if (transaction.getTransactionDate().isAfter(transaction.getScheduleDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction date must be on or before the schedule date.");
        }
        Transaction createdTransaction = service.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a transaction by ID", description = "Fetches the details of a transaction using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "id": 1,
                                          "amount": 1500,
                                          "transactionDate": "2025-01-15",
                                          "scheduleDate": "2025-01-20",
                                          "description": "Payment for services",
                                          "transferFee": 135.0
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "error": "Transaction not found."
                                        }
                                        """
                            )))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTransactionById(id));
    }

    @Operation(summary = "Get all transactions", description = "Retrieves a list of all transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        [
                                          {
                                            "id": 1,
                                            "amount": 1500,
                                            "transactionDate": "2025-01-15",
                                            "scheduleDate": "2025-01-20",
                                            "description": "Payment for services",
                                            "transferFee": 135.0
                                          },
                                          {
                                            "id": 2,
                                            "amount": 2500,
                                            "transactionDate": "2025-01-10",
                                            "scheduleDate": "2025-01-30",
                                            "description": "Large transfer",
                                            "transferFee": 172.5
                                          }
                                        ]
                                        """
                            )))
    })
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @Operation(summary = "Update an existing transaction", description = "Updates a transaction and recalculates the transfer fee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "id": 1,
                                          "amount": 2000,
                                          "transactionDate": "2025-01-15",
                                          "scheduleDate": "2025-01-20",
                                          "description": "Updated description",
                                          "transferFee": 180.0
                                        }
                                        """
                            ))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "error": "Transaction not found."
                                        }
                                        """
                            )))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(service.updateTransaction(id, transaction));
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                        {
                                          "error": "Transaction not found."
                                        }
                                        """
                            )))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
