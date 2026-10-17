package banksystem.controller;

import banksystem.dto.TransactionDTO;
import banksystem.service.TransactionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/admin")
    public ResponseEntity<List<TransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }
    @GetMapping("/me")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(Authentication authentication) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(authentication.getName()));
    }

}
