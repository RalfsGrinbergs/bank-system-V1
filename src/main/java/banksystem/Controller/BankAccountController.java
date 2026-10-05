package banksystem.Controller;

import banksystem.Dto.BankAccountDTO;
import banksystem.Service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
