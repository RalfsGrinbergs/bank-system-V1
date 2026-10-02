package banksystem.Dto;

import banksystem.Entity.User;

public record BankAccountDTO(
        Long accountNumber,
        String userEmail
) {

}
