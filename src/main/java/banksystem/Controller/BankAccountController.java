package banksystem.Controller;

import banksystem.Dto.BankAccountDTO;
import banksystem.Service.BankAccountService;
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
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(bankAccountService.findById(id));
    }
    @PostMapping("/{id}/deposits")
    public ResponseEntity<BankAccountDTO> deposit(
            @PathVariable @Positive Long id,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.deposit(id, sum));
    }
    @PostMapping("/{id}/withdraws")
    public ResponseEntity<BankAccountDTO> withdraw(
            @PathVariable @Positive Long id,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.withdraw(id, sum));
    }
    @PostMapping("/{id}/transfer/{receiverId}")
    public ResponseEntity<BankAccountDTO> transfer(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long receiverId,
            @RequestBody @NotNull @Positive @Digits(integer = 15, fraction = 2) BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.transfer(id, receiverId, sum));
    }
}
