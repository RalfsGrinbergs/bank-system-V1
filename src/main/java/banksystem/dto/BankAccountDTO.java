package banksystem.dto;

import java.math.BigDecimal;

public record BankAccountDTO(
        Long accountNumber,
        String userEmail,
        BigDecimal balance
) {

}
