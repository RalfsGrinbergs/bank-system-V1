package banksystem.Controller;

import banksystem.Dto.TransactionDTO;
import banksystem.Service.TransactionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable @Positive Long accountNumber) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountNumber));
    }

}
