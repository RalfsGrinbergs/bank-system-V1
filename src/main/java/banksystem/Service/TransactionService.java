package banksystem.Service;

import banksystem.Dto.TransactionDTO;
import banksystem.Entity.BankAccount;
import banksystem.Entity.Transactions;
import banksystem.Enum.TransactionsType;
import banksystem.Repository.TransactionRepository;
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
    public List<TransactionDTO> getAccountTransactions(Long id) {

       List<Transactions> transactions = transactionRepository.findByFromAccountIdOrToAccountId(id, id);
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



}