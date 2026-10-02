package banksystem.Service;

import banksystem.Dto.BankAccountDTO;
import banksystem.Dto.ResponseUserDTO;
import banksystem.Dto.UserDTO;
import banksystem.Entity.BankAccount;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Repository.BankAccountRepository;
import banksystem.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public BankAccountDTO createAccount(Long id ) {
        var accountNumber = new Random().nextLong(10_000, 100_000);
        User userForAccount = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User is not created"));

        if(!(userForAccount.getRole() == Role.USER)) {
            throw new IllegalStateException("User can't make a account"); // in future change to custom exception
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
    private BankAccountDTO toDomainBankAccount(BankAccount bankAccount) {
        return new BankAccountDTO(
                bankAccount.getAccountNumber(),
                bankAccount.getUser().getEmail()
        );
    }
}
