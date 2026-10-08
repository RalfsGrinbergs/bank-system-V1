package banksystem.dto;

import banksystem.enums.TransactionsType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        TransactionsType type,
        BigDecimal amount,
        LocalDateTime timestamp,
        Long fromAccountNumber,
        Long toAccountNumber
) {}