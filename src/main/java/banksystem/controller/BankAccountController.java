package banksystem.controller;

import banksystem.dto.BankAccountDTO;
import banksystem.service.BankAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "bearerAuth")
public class BankAccountController {
private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/me")
    public ResponseEntity<BankAccountDTO> createAccount(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankAccountService.createAccount(email));
    }
    @GetMapping("/admin")
    public ResponseEntity<List<BankAccountDTO>> findAll() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }
    @GetMapping("/me")
    public ResponseEntity<BankAccountDTO> findByUser(Authentication authentication) {
        return ResponseEntity.ok(bankAccountService.findByUser(authentication.getName()));
    }
    @PostMapping("/me/deposits")
    public ResponseEntity<BankAccountDTO> deposit(
            Authentication authentication,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.deposit(authentication.getName(), sum));
    }
    @PostMapping("/me/withdraws")
    public ResponseEntity<BankAccountDTO> withdraw(
            Authentication authentication,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.withdraw(authentication.getName(), sum));
    }
    @PostMapping("/me/transfer/{receiverAccountNumber}")
    public ResponseEntity<BankAccountDTO> transfer(
            Authentication authentication,
            @PathVariable @Positive Long receiverAccountNumber,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.transfer(authentication.getName(), receiverAccountNumber, sum));
    }
}
