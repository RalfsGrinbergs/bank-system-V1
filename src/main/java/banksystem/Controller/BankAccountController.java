package banksystem.Controller;

import banksystem.Dto.BankAccountDTO;
import banksystem.Service.BankAccountService;
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
    public ResponseEntity<BankAccountDTO> createAccount(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankAccountService.createAccount(id));
    }
    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> findAll() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.findById(id));
    }
    @PostMapping("/{id}/deposits")
    public ResponseEntity<BankAccountDTO> deposit(@PathVariable Long id, @RequestBody BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.deposit(id, sum));
    }
    @PostMapping("/{id}/withdraws")
    public ResponseEntity<BankAccountDTO> withdraw(@PathVariable Long id, @RequestBody BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.withdraw(id, sum));
    }
    @PostMapping("/{id}/transfer/{receiverId}")
    public ResponseEntity<BankAccountDTO> transfer(
            @PathVariable Long id,
            @PathVariable Long receiverId,
            @RequestBody BigDecimal sum) {
        return ResponseEntity.ok(bankAccountService.transfer(id, receiverId, sum));
    }
}
