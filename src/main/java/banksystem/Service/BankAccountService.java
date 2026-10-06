package banksystem.Service;

import banksystem.Dto.BankAccountDTO;
import banksystem.Entity.BankAccount;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Enum.TransactionsType;
import banksystem.Repository.BankAccountRepository;
import banksystem.Repository.UserRepository;
import banksystem.exceptions.RoleException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, TransactionService transactionService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }
    @Transactional
    public BankAccountDTO createAccount(Long id ) {
        var accountNumber = new Random().nextLong(10_000, 100_000);
        User userForAccount = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User is not created"));

        if(!(userForAccount.getRole() == Role.USER)) {
            throw new RoleException("User can't make a account");
        }
        var AccountToCreate = new BankAccount(
                userForAccount,
                accountNumber

        );
        userForAccount.setBankAccount(AccountToCreate);
        bankAccountRepository.save(AccountToCreate);
        userForAccount.setRole(Role.CLIENT);
        return toDomainBankAccount(AccountToCreate);
    }
    public List<BankAccountDTO> findAll() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        return accounts.stream()
                .map(this::toDomainBankAccount).toList();
    }
    public BankAccountDTO findByNumber(Long accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no account with account number " + accountNumber));
        return toDomainBankAccount(account);
    }
    private BankAccountDTO toDomainBankAccount(BankAccount bankAccount) {
        return new BankAccountDTO(
                bankAccount.getAccountNumber(),
                bankAccount.getUser().getEmail(),
                bankAccount.getBalance()

        );
    }
    @Transactional
    public BankAccountDTO deposit(Long accountNumber, BigDecimal sum) {
        BankAccount accountForDeposit = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("There is no account with that number " + accountNumber));
        accountForDeposit.deposit(sum);
        transactionService.createTransaction(
                null,
                accountForDeposit,
                sum,
                TransactionsType.DEPOSIT
        );
        return toDomainBankAccount(accountForDeposit);
     }
     @Transactional
    public BankAccountDTO withdraw(Long accountNumber, BigDecimal sum) {
        BankAccount accountForWithdraw = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no account with account number " + accountNumber));
        accountForWithdraw.withdraw(sum);
        transactionService.createTransaction(
                accountForWithdraw,
                null,
                sum,
                TransactionsType.WITHDRAW
        );
        return toDomainBankAccount(accountForWithdraw);


     }
     @Transactional
     public BankAccountDTO transfer(Long accountNumber, Long receiverAccountNumber, BigDecimal sum) {
        BankAccount sender = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no account with account number " + accountNumber));
        BankAccount receiver = bankAccountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no account with account number " + receiverAccountNumber));
        sender.transfer(sum, receiver);
         transactionService.createTransaction(
                 sender,
                 receiver,
                 sum,
                 TransactionsType.TRANSFER
         );
        return toDomainBankAccount(sender);

     }
}
