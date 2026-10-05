package banksystem.Dto;

import banksystem.Entity.User;

import java.math.BigDecimal;

public record BankAccountDTO(
        Long accountNumber,
        String userEmail,
        BigDecimal balance
) {

}
