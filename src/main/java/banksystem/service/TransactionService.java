package banksystem.service;

import banksystem.dto.TransactionDTO;
import banksystem.entity.BankAccount;
import banksystem.entity.Transactions;
import banksystem.enums.TransactionsType;
import banksystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

    }

    public void createTransaction(
            BankAccount fromAccount,
            BankAccount toAccount,
            BigDecimal amount,
            TransactionsType type
    ) {
        Transactions transaction = new Transactions(
                toAccount,
                fromAccount,
                LocalDateTime.now(),
                amount,
                type
        );

        transactionRepository.save(transaction);
    }
    public List<TransactionDTO> getAccountTransactions(Long accountNumber) {

       List<Transactions> transactions = transactionRepository
               .findByFromAccount_AccountNumberOrToAccount_AccountNumber(accountNumber, accountNumber);
        return transactions.stream()
                .map(this::toDomainTransactions).toList();
    }

    private TransactionDTO toDomainTransactions(Transactions transaction) { return new TransactionDTO(
            transaction.getId(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getTimestamp(),
            transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : null,
            transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null );
    }
    public List<TransactionDTO> findAll() {
        List<Transactions> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::toDomainTransactions).toList();
    }


}
