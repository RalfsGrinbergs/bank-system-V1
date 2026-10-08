package banksystem.controller;

import banksystem.dto.BankAccountDTO;
import banksystem.service.BankAccountService;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {
private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<BankAccountDTO> createAccount(@PathVariable @Positive Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankAccountService.createAccount(id));
    }
    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> findAll() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountDTO> findByAccountNumber(@PathVariable @Positive Long accountNumber) {
        return ResponseEntity.ok(bankAccountService.findByNumber(accountNumber));
    }
    @PostMapping("/{accountNumber}/deposits")
    public ResponseEntity<BankAccountDTO> deposit(
            @PathVariable @Positive Long accountNumber,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.deposit(accountNumber, sum));
    }
    @PostMapping("/{accountNumber}/withdraws")
    public ResponseEntity<BankAccountDTO> withdraw(
            @PathVariable @Positive Long accountNumber,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.withdraw(accountNumber, sum));
    }
    @PostMapping("/{accountNumber}/transfer/{receiverAccountNumber}")
    public ResponseEntity<BankAccountDTO> transfer(
            @PathVariable @Positive Long accountNumber,
            @PathVariable @Positive Long receiverAccountNumber,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.transfer(accountNumber, receiverAccountNumber, sum));
    }
}
