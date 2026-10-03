package banksystem.service;

import banksystem.Dto.BankAccountDTO;
import banksystem.Entity.BankAccount;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Repository.BankAccountRepository;
import banksystem.Repository.UserRepository;
import banksystem.Service.BankAccountService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BankAccountServiceTest {
    @Test
    void createAccountSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository);
        User user = new User(
                "example@gmail.com",
                "test-password",
                Role.USER
        );
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(bankAccountRepository.save(any(BankAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        BankAccountDTO add = bankAccountService.createAccount(user.getId());
        assertEquals("example@gmail.com", add.userEmail());

    }
    @Test
    void createAccountClient() {
        UserRepository userRepository = mock(UserRepository.class);
        BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);
        BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userRepository);
        User user = new User(
                "example@gmail.com",
                "test-password",
                Role.CLIENT
        );
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        assertThrows(
            IllegalStateException.class,
                () -> bankAccountService.createAccount(user.getId())
        );
    }
}
