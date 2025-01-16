package com.example.spring_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Schema(description = "Details about a transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Unique identifier of the transaction", example = "1")
    private Long id;


    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "The amount to be transferred", example = "1500.0")
    private Double amount;

    @NotNull(message = "Transaction date is required")
    @Schema(description = "Date when the transaction is created", example = "2025-01-15")
    private LocalDate transactionDate;

    @NotNull(message = "Schedule date is required")
    @Schema(description = "Date when the transaction is scheduled", example = "2025-01-20")
    private LocalDate scheduleDate;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Additional details about the transaction", example = "Payment for services")
    private String description;

    @Schema(description = "Calculated transfer fee based on scheduling rules", example = "45.0", accessMode = Schema.AccessMode.READ_ONLY)
    private Double transferFee;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public LocalDate getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(LocalDate scheduleDate) { this.scheduleDate = scheduleDate; }

    public Double getTransferFee() { return transferFee; }
    public void setTransferFee(Double transferFee) { this.transferFee = transferFee; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
